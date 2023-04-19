package com.example.bookstore.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentAdminManageBinding
import com.example.bookstore.fragments.admin.book.BookFragment
import com.example.bookstore.fragments.admin.category.CategoryFragment
import com.example.bookstore.fragments.admin.user.UserFragment
import com.example.bookstore.fragments.authorization.InitFragment

/**
 * A simple [Fragment] subclass.
 * Use the [AdminManageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminManageFragment : Fragment(R.layout.fragment_admin_manage), View.OnClickListener {
    private lateinit var binding: FragmentAdminManageBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminManageBinding.inflate(inflater, container, false)

        binding.categories.setOnClickListener(this)
        binding.books.setOnClickListener(this)
        binding.users.setOnClickListener(this)

//        childFragmentManager.beginTransaction().add(R.id.child_fragment_cont, InitFragment())

        return binding.root
    }

    fun insertNestedFragment(childFragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction();
        transaction.replace(R.id.child_fragment_cont, childFragment).addToBackStack(null).commit();
    }

    override fun onClick(view: View?) {
        val fragment = when(view) {
            binding.categories -> CategoryFragment()
            binding.books -> BookFragment()
            binding.users -> UserFragment()
            else -> InitFragment()
        }
        insertNestedFragment(fragment)
    }
}