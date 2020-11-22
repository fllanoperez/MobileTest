package com.example.marvel.views

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marvel.R
import com.example.marvel.databinding.FragmentCharacterListBinding
import com.example.marvel.utils.ApiResource
import com.example.marvel.utils.showSnackBar
import com.example.marvel.viewmodels.CharacterListViewModel
import com.example.marvel.views.adapters.CharacterListAdapter
import com.example.marvel.views.models.CharacterListUIModel
import com.example.marvel.views.models.CharacterUIModel
import com.hendraanggrian.recyclerview.widget.PaginatedRecyclerView


class CharacterList: Fragment() {

    private val viewModel: CharacterListViewModel by activityViewModels()
    private var binding: FragmentCharacterListBinding? = null

    private val LAYOUT_MANAGER_STATE = "LAYOUT_MANAGER_STATE"
    private var mLayoutManagerState: Parcelable? = null

    private var charactersAdapter: CharacterListAdapter? = null

    private val characterListCallback = object: CharacterListAdapter.ChararacterListInterface {
        override fun onCharacterClick(character: CharacterUIModel) {
            binding?.let {
                val navigation = CharacterListDirections.actionCharacterListToCharacterDetail(character)
                Navigation.findNavController(it.root).navigate(navigation)
            }
        }
    }

    private val characterListPagination = object: PaginatedRecyclerView.Pagination() {
        override fun onPaginate(page: Int) {
            viewModel.onPaginate(page)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(LAYOUT_MANAGER_STATE, mLayoutManagerState);
    }

    override fun onPause() {
        super.onPause()
        mLayoutManagerState = binding?.characterList?.layoutManager?.onSaveInstanceState()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_character_list, container, false)
        savedInstanceState?.let {
            mLayoutManagerState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE);
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCharacters()
    }

    private fun observeCharacters(){
        viewModel.characters.observe(viewLifecycleOwner, Observer{
            when(it){
                is ApiResource.Success -> showData(it.data)
                is ApiResource.Loading -> showLoading()
                is ApiResource.Error -> showError()
            }
        })
    }

    private fun showError() {
        binding?.root?.showSnackBar(getString(R.string.error_generic), getString(R.string.button_retry),
            View.OnClickListener { viewModel.retryGetCharacters() })
    }

    private fun showLoading() {
        binding?.let { view ->
            if(view.characterList.pagination?.state != PaginatedRecyclerView.Pagination.State.LOADING) {
                view.progressbar?.visibility = View.VISIBLE
            }
        }
    }

    private fun showData(data: CharacterListUIModel?){
        binding?.let { view ->
            view.progressbar.visibility = View.GONE
            data?.let { data ->
                charactersAdapter?.let { adapter ->
                        adapter.updateData(data.characters, viewModel.pageSize)
                    view.characterList.pagination?.notifyPageLoaded()
            } ?: run {
                view.characterList.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    charactersAdapter = CharacterListAdapter(data.characters, characterListCallback)
                    adapter = charactersAdapter
                    pagination = characterListPagination
                    layoutManager?.onRestoreInstanceState(mLayoutManagerState)
                }
                }
            }
        }
    }
}