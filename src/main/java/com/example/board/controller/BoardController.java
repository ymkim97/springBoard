package com.example.board.controller;

import com.example.board.dto.Board;
import com.example.board.dto.LoginInfo;
import com.example.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 게시물 목록을 보여준다.
    // 리턴하는 문자열은 템플릿 이름이다. (forward)
    @GetMapping("/")
    public String list(@RequestParam(name = "page", defaultValue = "1") int page, HttpSession session, Model model) { // HttpSession, Model은 Spring이 자동으로 넣어준다.
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        model.addAttribute("loginInfo", loginInfo);

        int totalCount = boardService.getTotalCount(); // 전체 게시물 수
        List<Board> list = boardService.getBoards(page); // page가 1,2,3,4 ....
        int pageCount = totalCount / 10;
        if (totalCount % 10 > 0) {
            pageCount++;
        }
        int currentPage = page;

        model.addAttribute("list", list);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("currentPage", currentPage);

        return "list";
    }

    @GetMapping("/board")
    public String board(@RequestParam("boardId") int boardId, Model model) {

        Board board = boardService.getBoard(boardId);
        model.addAttribute("board", board);
        return "board";
    }

    @GetMapping("/writeForm")
    public String writeForm(HttpSession session, Model model) {
        // 로그인 한 사용자만 글을 써야한다. 로그인을 하지 않았다면 리스트 보기로 자동 이동 시킨다.
        // 세션에서 로그인한 정보를 읽어들인다.

        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        if (loginInfo == null) {
            return "redirect:/loginform";
        }

        model.addAttribute("loginInfo", loginInfo);

        return "writeForm";
    }

    @PostMapping("/write")
    public String write(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            HttpSession session
    ) {
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        if (loginInfo == null) {
            return "redirect:/loginform";
        }
        boardService.addBoard(loginInfo.getUserId(), title, content);

        return "redirect:/";
    }

    @GetMapping("/delete")
    public String delete(
            @RequestParam("boardId") int boardId,
            HttpSession session
    ) {
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        if (loginInfo == null) {
            return "redirect:/loginform";
        }
        // loginInfo.getUserId() 사용자가 쓴 글일 경우에만 삭제.
        List<String> roles = loginInfo.getRoles();
        if (roles.contains("ROLE_ADMIN")) {
            boardService.deleteBoard(boardId);
        } else {
            boardService.deleteBoard(loginInfo.getUserId(), boardId);
        }
        return "redirect:/";
    }

    @GetMapping("/updateform")
    public String updateform(@RequestParam("boardId") int boardId, Model model, HttpSession session) {
        // boardId에 해당하는 정보를 읽어와서 updateform 템플릿에 전달한다.
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        if (loginInfo == null) {
            return "redirect:/loginform";
        }
        Board board = boardService.getBoard(boardId, false);
        model.addAttribute("board", board);
        model.addAttribute("loginInfo", loginInfo);
        return "updateform";
    }

    @PostMapping("/update")
    public String update(
            @RequestParam("boardId") int boardId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            HttpSession session
    ) {
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        if (loginInfo == null) {
            return "redirect:/loginform";
        }
        Board board = boardService.getBoard(boardId, false);
        if (board.getUserId() != loginInfo.getUserId()) {
            return "redirect:/board?boardId=" + boardId; // 글 보기로 이동한다.
        }
        boardService.updateBoard(boardId, title, content);
        return "redirect:/board?boardId=" + boardId;
    }
}
