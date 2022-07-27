package com.qlive.uikit.component;

import com.qlive.uikit.hook.KITFunctionInflaterFactory;
import com.qlive.uikitcore.QLiveComponent;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

import kotlin.jvm.internal.Intrinsics;
/**
 * 功能组件容器
 */
public class QLiveFunctionComponent {

    private QLiveComponent replaceComponent;
    private boolean isEnable;
    @NotNull
    private final QLiveComponent originComponent;

    public final boolean isEnable() {
        return this.isEnable;
    }

    /**
     * 功能组件是否可用
     * @param enable
     */
    public final void setEnable(boolean enable) {
        this.isEnable = enable;
        this.check();
    }

    /**
     * 替换功能组件
     * @param replaceComponent
     */
    public  void replace(@NotNull QLiveComponent replaceComponent) {
        this.replaceComponent = replaceComponent;
        this.check();
    }

    private  void check() {
        if (this.isEnable) {
            if (this.replaceComponent != null) {
                HashSet<QLiveComponent> var10000 = KITFunctionInflaterFactory.INSTANCE.getFunctionComponents();
                QLiveComponent var10001 = this.replaceComponent;
                Intrinsics.checkNotNull(var10001);
                var10000.add(var10001);
            } else {
                KITFunctionInflaterFactory.INSTANCE.getFunctionComponents().add(this.originComponent);
            }
        } else {
            QLiveComponent var4 = this.replaceComponent;
            if (var4 != null) {
                KITFunctionInflaterFactory.INSTANCE.getFunctionComponents().remove(var4);
            }
            KITFunctionInflaterFactory.INSTANCE.getFunctionComponents().remove(this.originComponent);
        }

    }

    public QLiveFunctionComponent(@NotNull QLiveComponent originComponent) {
        this.originComponent = originComponent;
        KITFunctionInflaterFactory.INSTANCE.getFunctionComponents().add(this.originComponent);
        this.isEnable = true;
    }
}
