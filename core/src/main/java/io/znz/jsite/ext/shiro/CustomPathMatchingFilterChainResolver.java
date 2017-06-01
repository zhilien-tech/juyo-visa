package io.znz.jsite.ext.shiro;

import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-25
 * <p>Version: 1.0
 */
public class CustomPathMatchingFilterChainResolver extends PathMatchingFilterChainResolver {

    private CustomDefaultFilterChainManager customDefaultFilterChainManager;

    public void setCustomDefaultFilterChainManager(CustomDefaultFilterChainManager customDefaultFilterChainManager) {
        this.customDefaultFilterChainManager = customDefaultFilterChainManager;
        setFilterChainManager(customDefaultFilterChainManager);
    }
    //项目不需要匹配所有所以不重写此方法
//    public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
//        FilterChainManager filterChainManager = getFilterChainManager();
//        if (!filterChainManager.hasChains()) {
//            return null;
//        }
//
//        String requestURI = getPathWithinApplication(request);
//
//        List<String> chainNames = new ArrayList<String>();
//        //the 'chain names' in this implementation are actually path patterns defined by the user.
//        // We just use them as the chain name for the FilterChainManager's requirements
//        for (String pathPattern : filterChainManager.getChainNames()) {
//            // If the path does match, then pass on to the subclass implementation for specific checks:
//            if (pathMatches(pathPattern, requestURI)) {
//                chainNames.add(pathPattern);
//            }
//        }
//
//        if (chainNames.size() == 0) {
//            return null;
//        }
//
//        return customDefaultFilterChainManager.proxy(originalChain, chainNames);
//    }
}
