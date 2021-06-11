package jp.co.sss.shop.controller.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;

/**
 * 登録機能(顧客)のコントローラクラス
 *
 * @author SystemShared
 */
/**
 * @author edu
 *
 */
@Controller
public class UserRegistCustomerController {

	/**
	 * 会員情報
	 */
	@Autowired
	UserRepository userRepository;

	@Autowired
	HttpSession session;

	/**
	 * 会員情報入力画面表示処理
	 *
	 * @param Model Viewとの値受渡し
	 * @return "user/regist/user_regist_input" 会員情報 登録入力画面へ
	 */
	@RequestMapping(path = "/user/regist/input", method = RequestMethod.GET)
	public String registInput(Model model) {

		return "user/regist/user_regist_input";
	}

	/**
	 * POSTメソッドを利用して会員情報入力画面に戻る処理
	 *
	 * @param form 会員情報
	 * @return "user/regist/user_regist_input" 会員情報 登録入力画面へ
	 */
	@RequestMapping(path = "/user/regist/input", method = RequestMethod.POST)
	public String registInput(UserForm form) {

		return "user/regist/user_regist_input";
	}

	/**
	 * 会員情報確認画面表示処理
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(path = "/user/regist/check", method = RequestMethod.POST)
	public String registCheck(Model model) {

		return "user/regist/user_regist_check";
	}

	/**
	 * 会員情報完了画面表示処理
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(path = "/user/regist/complete", method = RequestMethod.POST)
	public String registComplete(Model model) {

		return "user/regist/user_regist_complete";
	}
}
