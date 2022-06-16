package com.qncube.linveroominner

import android.content.Context
import com.alibaba.fastjson.util.ParameterizedTypeImpl
import com.niucube.rtm.RtmCallBack
import com.qncube.linveroominner.http.OKHttpService
import com.qncube.linveroominner.http.PageData
import com.qiniu.jsonutil.JsonUtils
import com.qiniu.qnim.QNIMManager
import com.qncube.liveroomcore.QLiveCallBack
import com.qncube.liveroomcore.been.QLiveUser
import com.qncube.liveroomcore.getCode

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

    fun loginUser(context: Context, callBack: QLiveCallBack<QLiveUser>) {
        backGround {
            doWork {
                val user = OKHttpService.get("/client/user/profile", null, InnerUser::class.java)
                var isQnIm = false
                isQnIm = try {
                    loginUser = user
                    QNIMManager.mRtmAdapter.isLogin
                    true
                } catch (e: NoClassDefFoundError) {
                    e.printStackTrace()
                    false
                }
                if (isQnIm) {
                    QNIMManager.init("cigzypnhoyno", context)
                    QNIMManager.mRtmAdapter.login(
                        user.userId,
                        user.imUid,
                        user.im_username,
                        user.im_password,
                        object : RtmCallBack {
                            override fun onSuccess() {
                                callBack.onSuccess(user)
                                loginUser = user
                            }

                            override fun onFailure(code: Int, msg: String) {
                                callBack.onError(code, msg)
                                // callBack.onSuccess(user)
                            }
                        }
                    )
                }
            }
            catchError {
                callBack.onError(it.getCode(), it.message)
            }
        }
    }

    fun updateUser(
        avatar: String,
        nickName: String,
        extensions: HashMap<String, String>?,
        callBack: QLiveCallBack<Void>
    ) {
        backGround {
            doWork {
                val user = QLiveUser()
                user.avatar = avatar
                user.nick = nickName
                user.extensions = extensions

                OKHttpService.put("/client/user/user", JsonUtils.toJson(user), Any::class.java)
                callBack.onSuccess(null)
            }
            catchError {
                callBack.onError(it.getCode(), it.message)
            }
        }
    }
}