package com.example.administrator.wanandroid.http.paging

import android.annotation.SuppressLint
import com.example.administrator.paging.paging.BaseDataSourceFactory
import com.example.administrator.paging.paging.BaseItemDataSource
import com.example.administrator.wanandroid.bean.ArticleBean
import com.example.administrator.wanandroid.http.Api
import com.example.library.helper.RxHelper
import java.util.concurrent.Executor

/**
 * @author  : Alex
 * @date    : 2018/10/29
 * @version : V 2.0.2
 */
class QueryArticleDataSource(val key :String,private val api: Api, retryExecutor: Executor) :BaseItemDataSource<Int, ArticleBean>(retryExecutor) {
    var page = 0

    override fun setKey(item: ArticleBean) = item.id

    @SuppressLint("CheckResult")
    override fun setLoadAfterResult(params: LoadParams<Int>, callback: LoadCallback<ArticleBean>) {
        api.queryArticle(page,key)
                .compose(RxHelper.rxSchedulerHelper())
                .subscribe({
                    page = it.data?.curPage!!
                    networkSuccess()
                    callback.onResult(it.data!!.datas!!)
                }, {
                    networkFailed(it.message, params, callback)
                })
    }

    @SuppressLint("CheckResult")
    override fun setLoadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<ArticleBean>) {
        api.queryArticle(page,key)
                .compose(RxHelper.rxSchedulerHelper())
                .subscribe({

                    page = it.data?.curPage!!
                    refreshSuccess()
                    networkSuccess()
                    callback.onResult(it?.data!!.datas!!)

                }, {
                    refreshFailed(it.message, params, callback)
                })
    }
}