package com.qlive.coreimpl.datesource

import android.content.Context
import com.alibaba.fastjson.util.ParameterizedTypeImpl
import com.qlive.rtm.RtmCallBack
import com.qlive.coreimpl.http.OKHttpService
import com.qlive.coreimpl.http.PageData
import com.qlive.jsonutil.JsonUtils
import com.qlive.coreimpl.http.NetBzException
import com.qlive.core.QLiveCallBack
import com.qlive.core.been.QLiveUser
import com.qlive.coreimpl.model.AppConfig
import com.qlive.coreimpl.util.getCode
import com.qlive.coreimpl.model.InnerUser
import com.qlive.coreimpl.util.backGround
import com.qlive.qnim.QNIMManager
import im.floo.floolib.BMXErrorCode
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserDataSource {

    companion object {
        lateinit var loginUser: QLiveUser
    }

    /**
     * 当前房间在线用户
     */
    suspend fun getOnlineUser(liveId: String, page_num: Int, page_size: Int): PageData<QLiveUser> {

        val p = ParameterizedTypeImpl(
            arrayOf(QLiveUser::class.java),
            PageData::class.java,
            PageData::class.java
        )

        val data: PageData<QLiveUser> = OKHttpService.get(
            "/client/live/room/user_list",
            HashMap<String, String>().apply {
                put("live_id", liveId)
                put("page_num", page_num.toString())
                put("page_size", page_size.toString())
            },
            null,
            p
        )
        return data
    }

    /**
     * 使用用户ID搜索房间用户
     *
     * @param uid
     */
    suspend fun searchUserByUserId(uid: String): QLiveUser {
        val p = ParameterizedTypeImpl(
            arrayOf(QLiveUser::class.java),
            List::class.java,
            List::class.java
        )
        return OKHttpService.get<List<QLiveUser>>(
            "/client/user/users",
            HashMap<String, String>().apply {
                put("user_ids", uid)
            },
            null,
            p
        )[0]
    }

    /**
     * 使用用户im uid 搜索用户
     *
     * @param imUid
     * @param callBack
     */
    suspend fun searchUserByIMUid(imUid: String): QLiveUser {
        val p = ParameterizedTypeImpl(
            arrayOf(QLiveUser::class.java),
            List::class.java,
            List::class.java
        )
        return OKHttpService.get<List<QLiveUser>>(
            "/client/user/imusers",
            HashMap<String, String>().apply {
                put("im_user_ids", imUid)
            },
            null,
            p
        )[0]
    }

    suspend fun getToken() = suspendCoroutine<String> { coroutine ->
        OKHttpService.tokenGetter!!.getTokenInfo(object : QLiveCallBack<String> {
            override fun onError(code: Int, msg: String?) {
                coroutine.resumeWithException(NetBzException(code, msg))
            }

            override fun onSuccess(data: String) {
                OKHttpService.token = data
                coroutine.resume(data)
            }
        })
    }

    suspend fun updateUser(
        avatar: String,
        nickName: String,
        extensions: Map<String, String>?
    ) {
        val user = QLiveUser()
        user.avatar = avatar
        user.nick = nickName
        user.extensions = extensions
        OKHttpService.put("/client/user/user", JsonUtils.toJson(user), Any::class.java)
        loginUser.avatar = avatar
        loginUser.nick = nickName
        loginUser.extensions - extensions
    }
}