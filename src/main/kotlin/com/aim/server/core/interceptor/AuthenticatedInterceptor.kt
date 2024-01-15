//package com.aim.server.core.interceptor
//
//import com.aim.server.core.annotation.IsAuthenticated
//import com.aim.server.core.exception.BaseException
//import com.aim.server.core.exception.ErrorCode
//import com.aim.server.domain.admin.const.ConfigConsts.Companion.LOGIN_SESSION
//import jakarta.servlet.http.HttpServletRequest
//import jakarta.servlet.http.HttpServletResponse
//import org.springframework.stereotype.Component
//import org.springframework.web.method.HandlerMethod
//import org.springframework.web.servlet.HandlerInterceptor
//
//@Component
//class AuthenticatedInterceptor : HandlerInterceptor {
//    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
//        if (handler is HandlerMethod) {
//            val isAuthenticated = handler.hasMethodAnnotation(IsAuthenticated::class.java)
//            if (isAuthenticated) {
//                val session = request.getSession(false)
//                val isLogin = session?.getAttribute(LOGIN_SESSION)
//                if (isLogin == null || !(isLogin as Boolean)) {
//                    throw BaseException(ErrorCode.USER_NOT_LOGGED_IN)
//                }
//            }
//            return true
//        }
//        return false
//    }
//}