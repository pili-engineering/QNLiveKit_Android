package com.qlive.core;

import com.qlive.core.been.QInvitation;

public interface QInvitationHandlerListener {
    void onReceivedApply(QInvitation invitation);   //收到申请/邀请
    void onApplyCanceled(QInvitation invitation);   //对方取消申请
    void onApplyTimeOut(QInvitation invitation);    //申请/邀请 超时
    void onAccept(QInvitation invitation);          //被接受
    void onReject(QInvitation invitation);          //被拒绝
}
