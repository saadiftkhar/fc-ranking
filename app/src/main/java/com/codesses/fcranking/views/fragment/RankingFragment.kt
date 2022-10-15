package com.codesses.fcranking.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.codesses.fcranking.R
import com.codesses.fcranking.databinding.FragmentRankingBinding
import com.codesses.fcranking.model.Characters
import com.codesses.fcranking.utils.getView
import com.codesses.fcranking.utils.launchPeriodicAsync
import com.codesses.fcranking.utils.visible
import com.codesses.fcranking.viewmodel.CharactersViewModel
import com.codesses.fcranking.views.adapter.RankingAdapter
import com.codesses.fcranking.views.dialog.ProgressDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers

class RankingFragment : Fragment() {

    lateinit var viewModel: CharactersViewModel

    lateinit var mContext: FragmentActivity

    lateinit var binding: FragmentRankingBinding

    lateinit var navigationController: NavController

    lateinit var job: Deferred<Unit>


    lateinit var charactersList: MutableList<Characters>

    var isFirst = true

    private val rankingAdapter: RankingAdapter by lazy {
        RankingAdapter(mContext, isFirst) {
            val navDirections =
                RankingFragmentDirections.actionNvRankingToCharacterProfileFragment(it)
            navigationController.navigate(navDirections)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = requireActivity()
        viewModel = ViewModelProvider(
            mContext,
            ViewModelProvider.AndroidViewModelFactory.getInstance(mContext.application)
        ).get(CharactersViewModel::class.java)

        charactersList = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            FragmentRankingBinding.bind(getView(R.layout.fragment_ranking, mContext, container!!))

        navigationController = findNavController()

        //  Recyclerview
        binding.rvCharacters.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = rankingAdapter
        }

        job = CoroutineScope(Dispatchers.IO).launchPeriodicAsync(15000) {
            if (isFirst) {
                rankingAdapter.isFirst = isFirst
                isFirst = false
            } else {
                rankingAdapter.isFirst = isFirst
            }
            viewModel.getCharacters()
        }

        observer()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            job.cancel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun observer() {
        viewModel.characters.observe(mContext) { charactersList ->
            binding.progressBar.visible(false)
            if (charactersList.isNotEmpty()) {
                this.charactersList.clear()
                binding.rvCharacters.visible(true)
//                val list = charactersList.sortedByDescending { it.rank }
                this.charactersList.addAll(charactersList)
                rankingAdapter.submitList(this.charactersList)
                rankingAdapter.notifyDataSetChanged()
            }
        }
    }


}