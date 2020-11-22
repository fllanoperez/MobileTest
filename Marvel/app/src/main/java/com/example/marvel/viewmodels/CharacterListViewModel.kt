package com.example.marvel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.marvel.api.MarvelRepository
import com.example.marvel.models.MarvelResponse
import com.example.marvel.models.Thumbnail
import com.example.marvel.utils.ApiResource
import com.example.marvel.views.models.CharacterListUIModel
import com.example.marvel.views.models.CharacterUIModel
import com.example.marvel.views.models.ComicUIModel

class CharacterListViewModel: ViewModel() {

    private val _characters = MutableLiveData<ApiResource<CharacterListUIModel>>()
    val characters: LiveData<ApiResource<CharacterListUIModel>> = _characters

    val pageSize = 30 //MAX 100
    var characterList: CharacterListUIModel = CharacterListUIModel(emptyList())

    private val characterObserver = Observer<ApiResource<MarvelResponse>> {
        when(it) {
            is ApiResource.Success -> postSuccess(it.data)
            is ApiResource.Error -> postError(it.message)
            is ApiResource.Loading -> _characters.postValue(ApiResource.Loading())
        }
    }

    init {
        observeCharacters()
    }

    override fun onCleared() {
        super.onCleared()
        MarvelRepository.getInstance().characters.removeObserver(characterObserver)
    }

    fun onPaginate(){
        _characters?.value?.data?.characters?.let {
            MarvelRepository.getInstance().getCharacters(it.size, pageSize)
            _characters.value = ApiResource.Loading()
        }
    }

    fun retryGetCharacters() {
        MarvelRepository.getInstance().getCharacters(0)
    }

    fun isMoreItems(): Boolean {
        getTotalItems()?.let { totalItems ->
            return totalItems > characterList.characters.size
        }
        return true
    }

    private fun observeCharacters() {
        MarvelRepository.getInstance().characters.observeForever(characterObserver)
        MarvelRepository.getInstance().getCharacters(0)
    }

    private fun getTotalItems(): Int? = MarvelRepository.getInstance().characters.value?.data?.data?.total

    private fun postError(error: String?) {
        _characters.postValue(ApiResource.Error(error ?: ""))
    }

    private fun postSuccess(response: MarvelResponse?) {
        response?.let {
            characterList = convertResponse(it, characterList)
            _characters.postValue(ApiResource.Success(characterList))
        } ?: run {
            _characters.postValue(ApiResource.Error(""))
        }
    }

    private fun convertResponse(response: MarvelResponse, appendTo: CharacterListUIModel): CharacterListUIModel{
        val characterList = ArrayList<CharacterUIModel>()
        characterList.addAll(appendTo.characters)
        for (character in response.data.results){
            val comicList = ArrayList<ComicUIModel>()
            for (comic in character.comics.items){
                comicList.add(ComicUIModel(comic.name))
            }
            characterList.add(CharacterUIModel(getImageURL(character.thumbnail), character.name, character.description, comicList))
        }
        return CharacterListUIModel(characterList)
    }

    private fun getImageURL(thumbnail: Thumbnail): String {
        return thumbnail.path +"."+ thumbnail.extension
    }
}