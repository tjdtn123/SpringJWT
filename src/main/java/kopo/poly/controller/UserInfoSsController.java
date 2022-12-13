package kopo.poly.controller;

import kopo.poly.auth.AuthInfo;
import kopo.poly.auth.UserRole;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.service.IUserInfoSsService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequestMapping(value = "/ss")
@RequiredArgsConstructor
@Controller
public class UserInfoSsController {

    private final IUserInfoSsService userInfoSsService;

    private final PasswordEncoder bCryptPasswordEncoder;

    @GetMapping(value = "userRegForm")
    public String userRegForm() {

        log.info(this.getClass().getName() + ".ss/userRegForm ok!");

        return "/ss/UserRegForm";
    }

    @RequestMapping(value = "insertUserInfo")
    public String insertUserInfo(HttpServletRequest request, ModelMap model) throws Exception{

        log.info(this.getClass().getName() + ".insertUserInfo Start!");

        String msg = "";

        UserInfoDTO pDTO = null;

        try {

            String user_id = CmmUtil.nvl(request.getParameter("user_id"));
            String user_name = CmmUtil.nvl(request.getParameter("user_name"));
            String password = CmmUtil.nvl(request.getParameter("password"));
            String email = CmmUtil.nvl(request.getParameter("email"));
            String addr1 = CmmUtil.nvl(request.getParameter("addr1"));
            String addr2 = CmmUtil.nvl(request.getParameter("addr2"));

            log.info("userId : " + user_id);
            log.info("userName : " + user_name);
            log.info("password : " + password);
            log.info("email : " + email);
            log.info("addr1 : " + addr1);
            log.info("addr2 : " + addr2);

            pDTO = new UserInfoDTO();

            pDTO.setUserId(user_id);
            pDTO.setUserName(user_name);

            pDTO.setPassword(bCryptPasswordEncoder.encode(password));

            pDTO.setRoles(UserRole.USER.getValue());

            int res = userInfoSsService.insertUserInfo(pDTO);

            log.info("회원가입 결과(res) : " + res);

            if (res == 1) {
                msg = "회원가입되었습니다.";

            } else if (res == 2) {
                msg = "이미 가입된 아이디입니다.";

            } else {
                msg = "오류로 인해 회우너가입이 실패하였습니다.";

            }

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e;
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            log.info(this.getClass().getName() + ".insertUserInfo End!");

            model.addAttribute("msg", msg);

            model.addAttribute("pDTO", pDTO);

            pDTO = null;

        }
        return "/user/UserRegSuccess";


    }

    @GetMapping(value = "loginForm")
    public String loginForm(){
        log.info(this.getClass().getName() + ".user/loginForm ok!");

        return "/ss/LoginForm";
    }

    @RequestMapping(value = "loginSuccess")
    public String loginSuccess(@AuthenticationPrincipal AuthInfo authInfo, ModelMap model) {

        UserInfoDTO dto = authInfo.getUserInfoDTO();

        String userName = CmmUtil.nvl(dto.getUserName());
        String userId = CmmUtil.nvl(dto.getUserId());

        log.info("userName : ", userName);
        log.info("userId", userId);

        model.addAttribute("userName", userName);
        model.addAttribute("userId", userId);

        return "/ss/LoginSuccess";
    }

    @RequestMapping(value = "loginFail")
    public String loginFail() {

        return "/ss/LoginFail";
    }

    @GetMapping(value = "logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        new SecurityContextLogoutHandler().logout(
                request, response, SecurityContextHolder.getContext().getAuthentication());

        return "/";
    }
}
