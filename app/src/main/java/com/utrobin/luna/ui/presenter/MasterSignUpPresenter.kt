package com.utrobin.luna.ui.presenter

import com.apollographql.apollo.rx2.Rx2Apollo
import com.utrobin.luna.App
import com.utrobin.luna.CreateMasterMutation
import com.utrobin.luna.network.GraphQLService
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.ui.contract.MasterSignUpContract
import com.utrobin.luna.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by ivan on 08.02.2018.
 */

class MasterSignUpPresenter : BasePresenter<MasterSignUpContract.View>(), MasterSignUpContract.Presenter {

    @Inject
    lateinit var graphQLService: GraphQLService

    init {
        App.component.injectMasterSignUpPresenter(this)
    }

    override fun onSignUpButtonClicked() {
        if (view?.validateFields() != true) {
            return
        }

        val mutation = CreateMasterMutation
                .builder()
                .username(view?.getUsername()!!)
                .name(view?.getName()!!)
                .email(view?.getEmail()!!)
                .password(view?.getPassword()!!)
                .lat("")
                .lon("")
                .build()


        val apolloCall = graphQLService.apolloClient.mutate(mutation)
        Rx2Apollo.from(apolloCall)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view?.showProgressBar(true) }
                .doOnTerminate { view?.showProgressBar(false) }
                .subscribe(
                        {
                            view?.signUpFinished()
                        },
                        {
                            LogUtils.logException(FeedPresenter::class.java, it)
                            view?.signUpFailed(NetworkError.UNKNOWN)
                        }
                )
    }

    override fun isLoginCorrect(login: String): Boolean {
        return login.length >= 5
    }

    override fun isNameCorrect(name: String): Boolean {
        return name.length >= 2
    }

    override fun isEmailCorrect(email: String): Boolean {
        return email.length >= 5
    }

    override fun isPasswordCorrect(password: String): Boolean {
        return password.length >= 6
    }

    override fun destroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}