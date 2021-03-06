package customer.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import bean.Customer;
import common.controller.SuperClass;
import dao.CustomerDao;
//회원 이메일 찾기에 대한 컨트롤러입니다.
@Controller
public class CustPWSearchController extends SuperClass {
	private final String command = "/custPWSearch.cu";
	private final String redirect = "redirect:/custPWResult.cu";

	private ModelAndView mav = null;

	@Autowired
	@Qualifier("cdao")
	private CustomerDao cdao;

	public CustPWSearchController() {
		super("custPWSearch", null);
		this.mav = new ModelAndView();
	}

	// '비밀번호 찾기' 클릭
	@GetMapping(command)
	public ModelAndView doGet() {
		this.mav.setViewName(super.getpage);
		return this.mav;
	}

	// 이메일 찾기 페이지에서 "찾기" 버튼 클릭
	@PostMapping(command)
	public ModelAndView doPost(
			// 이메일, 이름, 연락처, 생년월일 등등 4개의 데이터로 일치하는 회원 정보를 조회합니다.
			@RequestParam(value = "cust_Email", required = false) String cust_Email,
			@RequestParam(value = "cust_Name", required = false) String cust_Name,
			@RequestParam(value = "cust_Contact", required = false) String cust_Contact,
			@RequestParam(value = "cust_Birth", required = false) int cust_Birth,
			HttpSession session) {

		boolean isCheck = true;
		System.out.println("가입정보를 위한 email, name, contact, birth 입력됨");

		if (isCheck == true) {
			Customer bean = this.cdao.SelectPW(cust_Email, cust_Name, cust_Contact, cust_Birth);
			if (bean == null) { 
				System.out.println("일치하는 회원정보가 없음");
				this.mav.setViewName("redirect:/custPWSearch.cu");
				session.setAttribute("message", "일치하는 회원정보가 없습니다!");

			} else { // 일치하는 회원정보 찾음
				// 조회하려는 정보를 세션 영역에 바인딩
				// 이 내용은 common.jsp 파일에서 참조
				session.setAttribute("bean", bean);
				this.mav.setViewName(redirect);
			}

		} else { // 문제있음
			this.mav.addObject("cust_Email", cust_Email);
			this.mav.addObject("cust_Name", cust_Name);
			this.mav.addObject("cust_Contact", cust_Contact);
			this.mav.addObject("cust_Birth", cust_Birth);
			this.mav.setViewName("redirect:/custPWSearch.cu");
			session.setAttribute("message", "일치하는 회원정보가 없습니다!");		
			}
		return this.mav;
	}
}