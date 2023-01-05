package com.asmirestoran.pesomakanan.ui.adapter

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.asmirestoran.pesomakanan.R
import com.asmirestoran.pesomakanan.Resource
import com.asmirestoran.pesomakanan.Status
import com.asmirestoran.pesomakanan.databinding.FragmentAddItemBinding
import com.asmirestoran.pesomakanan.model.Category
import com.asmirestoran.pesomakanan.model.Item
import com.asmirestoran.pesomakanan.ui.Utils
import com.asmirestoran.pesomakanan.ui.BaseFragment
import com.asmirestoran.pesomakanan.viewmodel.CategoryViewModel
import com.asmirestoran.pesomakanan.viewmodel.AddItemViewModel

val permissions: Array<String> = arrayOf(
    android.Manifest.permission.CAMERA,
    android.Manifest.permission.READ_EXTERNAL_STORAGE,
    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
)

/**
 * A simple [Fragment] subclass.
 * Use the [AddItemFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddItemFragment : BaseFragment() {
    private lateinit var categoryImage: Bitmap
    private var clickedId: Int = 0
    private lateinit var imageCaptureLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    lateinit var binding: FragmentAddItemBinding
    private val addItemViewModel: AddItemViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    var error: String = ""
    lateinit var categoryAdapter: CategorySpinnerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_item, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.itemViewModel = addItemViewModel
        initViews()
        observe()
    }

    private fun initViews() {
        binding.ivCamera.setOnClickListener {
            clickedId = binding.ivCamera.id
            requestPermissionLauncher.launch(permissions)
        }
        binding.ivGallery.setOnClickListener {
            clickedId = binding.ivGallery.id
            requestPermissionLauncher.launch(permissions)
        }
        binding.btnAdd.setOnClickListener {
            val error = Item.valid(addItemViewModel.item.value)
            binding.error = error
            if (error.isEmpty()) {
                addItemViewModel.addItem()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        categoryViewModel.categoryListResult.removeObservers(viewLifecycleOwner)
        addItemViewModel.addItemResult.removeObservers(viewLifecycleOwner)
    }


    private fun observe() {
        categoryViewModel.categoryListResult.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    showProgress(true)
                }
                Status.SUCCESS -> {
                    showProgress(false)
                    onCategoriesLoaded(resource)
                }
                Status.ERROR -> {
                    showProgress(false)
                }
            }
        }
        addItemViewModel.addItemResult.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    showProgress(true)
                }
                Status.SUCCESS -> {
                    showProgress(false)
                    Utils.showSnack(
                        requireActivity(),
                        "Added item ${it.data?.name}"
                    )
                }
                Status.ERROR -> {
                    showProgress(false)
                    Utils.showSnack(requireActivity(), it.message)
                }
            }
        }
        addItemViewModel.uploadImageResult.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    showProgress(true)
                }
                Status.SUCCESS -> {
                    showProgress(false)
                    Utils.showSnack(
                        requireActivity(),
                        "Uploaded image ${it.data}"
                    )
                    it.data?.let { path ->
                        addItemViewModel.item.value?.imageURL = path
                    }
                }
                Status.ERROR -> {
                    showProgress(false)
                    Utils.showSnack(requireActivity(), it.message)
                }
            }
        }
    }

    private fun onCategoriesLoaded(resource: Resource<List<Category>>) {
        resource.data?.let {
            if (it.isNotEmpty()) {
                categoryAdapter = CategorySpinnerAdapter(requireContext(), it)
                binding.spCategory.adapter = categoryAdapter
                binding.spCategory.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            p3: Long
                        ) {
                            addItemViewModel.item.value?.category =
                                categoryAdapter.getItem(position)
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {}
                    }
            }
        }
    }

    private fun handlePermissionResult(permissionsMap: Map<String, Boolean>) {
        if (isAllPermissionsEnabled(permissionsMap)) {
            when (clickedId) {
                binding.ivCamera.id -> openCamera()
                binding.ivGallery.id -> openGallery()
            }
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageCaptureLauncher.launch(cameraIntent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imageCaptureLauncher.launch(intent)
    }

    private fun isAllPermissionsEnabled(grantedPermissions: Map<String, Boolean>): Boolean {
        grantedPermissions.onEach {
            if (!it.value) {
                return false
            }
        }
        return true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initLaunchers()
    }

    private fun initLaunchers() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grantedPermissions ->
                handlePermissionResult(grantedPermissions)
            }

        imageCaptureLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK && it.data != null) {
                    if (clickedId == binding.ivCamera.id) {
                        categoryImage = it.data?.extras?.get("data") as Bitmap
                        categoryImage = Utils.getResizedBitmap(
                            categoryImage,
                            binding.ivImage.width,
                            binding.ivImage.height
                        )
                        binding.ivImage.setImageBitmap(categoryImage)
                        uploadImageToFirebase(categoryImage)
                    } else if (clickedId == binding.ivGallery.id) {
                        val uri = it.data?.data
                        uri?.let {
                            var categoryImage =
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                    ImageDecoder.decodeBitmap(
                                        ImageDecoder.createSource(
                                            requireContext().contentResolver,
                                            it
                                        )
                                    )
                                } else {
                                    MediaStore.Images.Media.getBitmap(
                                        requireContext().contentResolver,
                                        it
                                    )
                                }
                            categoryImage = Utils.getResizedBitmap(
                                categoryImage,
                                binding.ivImage.width,
                                binding.ivImage.height
                            )
                            binding.ivImage.setImageBitmap(categoryImage)
                            uploadImageToFirebase(categoryImage)
                        }

                    }

                }
            }
    }

    private fun uploadImageToFirebase(categoryImage: Bitmap) {
        addItemViewModel.uploadImage(categoryImage)
    }

    override fun getProgressBar(): View {
        return binding.pb.root
    }
}