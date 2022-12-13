package kopo.poly.controller;


import kopo.poly.dto.NoticeDTO;
import kopo.poly.service.INoticeService;
import kopo.poly.util.CmmUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping(value = "/notice")
@Controller
public class NoticeController {

    @Resource(name = "NoticeService")
    public INoticeService noticeService;


    @GetMapping(value = "noticeList")
    public String noticeList(ModelMap model){

        log.info(this.getClass().getName() + ".noticeList start!");

        List<NoticeDTO> rList = noticeService.getNoticeList();

        if (rList == null) {
            rList = new ArrayList<NoticeDTO>();

        }

        model.addAttribute("rList", rList);

        rList = null;

        log.info(this.getClass().getName() + ".noticeList end!");

        return "/notice/NoticeList";

    }

    @GetMapping(value = "noticeInfo")
    public String noticeInfo(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".noticeInfo Start!");

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));

        log.info("nSeq : " + nSeq);

        NoticeDTO pDTO = new NoticeDTO();
        pDTO.setNoticeSeq(Long.parseLong(nSeq));

        NoticeDTO rDTO = noticeService.getNoticeInfo(pDTO, true);

        if (rDTO == null) {
            rDTO = new NoticeDTO();

        }

        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".noticeInfo End!");

        return "/notice/NoticeInfo";

    }

    @GetMapping(value = "noticeEditInfo")
    public String noticeEditInfo(HttpServletRequest request, ModelMap model) {

        log.info(this.getClass().getName() + ".noticeEditInfo Start!");

        String msg = "";

        try {
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));

            log.info("nSeq : " + nSeq);
            NoticeDTO pDTO = new NoticeDTO();

            pDTO.setNoticeSeq(Long.parseLong(nSeq));

            NoticeDTO rDTO = noticeService.getNoticeInfo(pDTO, false);

            if ( rDTO == null){

                rDTO = new NoticeDTO();
            }

            model.addAttribute("rDTO", rDTO);

        } catch (Exception e) {
            msg = "실패하셨습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            log.info(this.getClass().getName() + ".NoticeUpdate end!");

            model.addAttribute("msg", msg);

        }

        log.info(this.getClass().getName() + ".noticeEditInfo end!");

        return "/notice/NoticeEditInfo";
    }

    @PostMapping(value = "noticeUpdate")
    public String NoticeUpdate(HttpServletRequest request, ModelMap model) {

        log.info(this.getClass().getName() + ".noticeUpdate Start!");

        String msg = "";

        try {


            String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));
            String title = CmmUtil.nvl(request.getParameter("title"));
            String noticeYn = CmmUtil.nvl(request.getParameter("noticeYn"));
            String contents = CmmUtil.nvl(request.getParameter("contents"));


            log.info("nSeq : " + nSeq);
            log.info("title : " + title);
            log.info("noticeYn : " +noticeYn);
            log.info("contents : " + contents);

            NoticeDTO pDTO = new NoticeDTO();


            pDTO.setNoticeSeq(Long.parseLong(nSeq));
            pDTO.setTitle(title);
            pDTO.setNoticeYn(noticeYn);
            pDTO.setContents(contents);

            noticeService.updateNoticeInfo(pDTO);

            msg = "수정되었습니다.";
        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            log.info(this.getClass().getName() + ".noticeUpdate End!");

            model.addAttribute("msg",msg);

        }

        return "/notice/MsgToList";
    }

    @GetMapping(value = "noticeDelete")
    public String noticeDelete(HttpServletRequest request, ModelMap model) {

        log.info(this.getClass().getName() + ".noticeDelete Start!");

        String msg = "";

        try {
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));

            log.info("nSeq ; " + nSeq);

            NoticeDTO pDTO = new NoticeDTO();

            pDTO.setNoticeSeq(Long.parseLong(nSeq));

            noticeService.deleteNoticeInfo(pDTO);

            msg = "삭제되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        }finally {
            log.info(this.getClass().getName() + ".noticeDeleteEnd!");

            model.addAttribute("msg", msg);

        }

        return "/notice/MsgToList";
    }

    @GetMapping(value = "noticeReg")
    public String noticeReg() {

        log.info(this.getClass().getName() + ".noticeReg Start!");

        log.info(this.getClass().getName()  + ".noticeReg End!");

        return "/notice/NoticeReg";
    }

    @PostMapping(value = "noticeInsert")
    public String noticeInsert(HttpServletRequest request, ModelMap model) {

        log.info(this.getClass().getName() + ".noticeInsert Start!");

        String msg = "";

        try {
            /*
             * 게시판 글 등록되기 위해 사용되는 form객체의 하위 input 객체 등을 받아오기 위해 사용함
             */

            String title = CmmUtil.nvl(request.getParameter("title")); // 제목
            String noticeYn = CmmUtil.nvl(request.getParameter("noticeYn")); // 공지글 여부
            String contents = CmmUtil.nvl(request.getParameter("contents")); // 내용

            /*
             * ####################################################################################
             * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함 반드시 작성할 것
             * ####################################################################################
             */

            log.info("title : " + title);
            log.info("noticeYn : " + noticeYn);
            log.info("contents : " + contents);

            NoticeDTO pDTO = new NoticeDTO();


            pDTO.setTitle(title);
            pDTO.setNoticeYn(noticeYn);
            pDTO.setContents(contents);

            /*
             * 게시글 등록하기위한 비즈니스 로직을 호출
             */
            noticeService.InsertNoticeInfo(pDTO);

            // 저장이 완료되면 사용자에게 보여줄 메시지
            msg = "등록되었습니다.";


        } catch (Exception e) {

            // 저장이 실패되면 사용자에게 보여줄 메시지
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            log.info(this.getClass().getName() + ".noticeInsert End!");

            // 결과 메시지 전달하기
            model.addAttribute("msg", msg);

        }

        return "/notice/MsgToList";
    }



}
