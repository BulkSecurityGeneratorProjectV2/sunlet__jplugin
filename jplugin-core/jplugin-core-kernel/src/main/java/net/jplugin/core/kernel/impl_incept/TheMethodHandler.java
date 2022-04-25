package net.jplugin.core.kernel.impl_incept;

import javassist.util.proxy.MethodHandler;
import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.common.kits.filter.FilterManager;
import net.jplugin.common.kits.filter.IFilter;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.ExtensionInterceptorContext;
import net.jplugin.core.kernel.api.IExtensionInterceptor;
import net.jplugin.core.kernel.api.PluginEnvirement;

import java.lang.reflect.Method;
import java.util.List;

public class TheMethodHandler implements MethodHandler, IFilter<ExtensionInterceptorContext> {
    private final Extension extension;
    private Object objectInstance;
    FilterManager<ExtensionInterceptorContext> filterManager = null;


    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        try {
            //启动阶段会
            if (PluginEnvirement.getInstance().getStateLevel() < PluginEnvirement.STAT_LEVEL_INITING){
//            if (filterManager == null || thisMethod.getDeclaringClass().equals(Object.class)) {
                return proceed.invoke(self, args);
            } else {
                ExtensionInterceptorContext ctx = new ExtensionInterceptorContext();
                ctx.init(this.extension, thisMethod, args, proceed);

                return filterManager.filter(ctx);
            }
        }catch(Throwable e){
            e.printStackTrace();
            throw e;
        }
    }

    public TheMethodHandler(Extension ext){
        this.extension = ext;
    }

    public void setObjectInstance(Object o){
        this.objectInstance = o;
    }

    public void initFilters(List<IExtensionInterceptor> filters){
        filterManager = new FilterManager<>();
        for (IFilter f:filters){
            filterManager.addFilter(f);
        }
        filterManager.addFilter(this);
    }


    @Override
    public Object filter(FilterChain fc, ExtensionInterceptorContext ctx) throws Throwable {
        return ctx._get_procceedMethod().invoke(this.objectInstance,ctx.getArgs());
    }
}
