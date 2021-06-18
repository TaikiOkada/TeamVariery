package jp.co.sss.shop.controller.user;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Prefecture;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.PrefectureRepository;
import jp.co.sss.shop.repository.UserRepository;

/**
 * 登録機能(一般会員用)のコントローラクラス
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
	PrefectureRepository prefectureRepository;

	@Autowired
	HttpSession session;

	/**
	 * 会員情報入力画面表示処理
	 *
	 * @param Model Viewとの値受渡し
	 * @return "user/regist/user_regist_input" 会員情報 登録入力画面へ
	 */
	@RequestMapping(path = "/user/regist/input", method = RequestMethod.GET)
	public String registInput(@ModelAttribute UserForm form, Model model) {

		model.addAttribute("prefectures",prefectureRepository.findAll());
		return "user/regist/user_regist_input";
	}

	/**
	 * POSTメソッドを利用して会員情報入力画面に戻る処理
	 *
	 * @param form 会員情報
	 * @return "user/regist/user_regist_input" 会員情報 登録入力画面へ
	 */
	@RequestMapping(path = "/user/regist/input", method = RequestMethod.POST)
	public String registInput(Model model) {

		UserForm userForm = new UserForm();

		model.addAttribute("userForm", userForm);

		model.addAttribute("prefectures",prefectureRepository.findAll());
		//入力画面に戻る時に、入力した値を保持する。
		return registInput(userForm,model);
		//入力画面に戻る時に、入力した値を捨てる。。
		//return "user/regist/user_regist_input";
	}

	/**
	 * 会員情報確認画面表示処理
	 *
	 * @param form   会員情報フォーム
	 * @param result 入力チェック結果
	 * @return 入力値エラーあり："user/regist/user_regist_input" 会員情報登録画面へ
	 *         入力値エラーなし："user/regist/user_regist_check" 会員情報 登録確認画面へ
	 */
	@RequestMapping(path = "/user/regist/check", method = RequestMethod.POST)
	public String registCheck(@Valid @ModelAttribute UserForm form, BindingResult result, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("prefectures",prefectureRepository.findAll());
			return "user/regist/user_regist_input";
		}

		model.addAttribute("prefecture", prefectureRepository.getOne(form.getPrefectureId()));


		return "user/regist/user_regist_check";


	}

	/**
	 * 会員情報登録完了処理
	 *
	 * @param form 会員情報
	 * @return "redirect:/user/regist/complete" 会員情報 登録完了画面へ
	 */
	@RequestMapping(path = "/user/regist/complete", method = RequestMethod.POST)
	public String registComplete(@ModelAttribute UserForm form) {

		System.out.println("メソッド開始");

		// 会員情報の生成
		User user = new User();
		UserBean userBean= new UserBean();

		System.out.println("データベース前");

		Prefecture prefecture = prefectureRepository.getOne(form.getPrefectureId());

		System.out.println("name = " + prefecture.getName());
		// 入力値を会員情報にコピー
		BeanUtils.copyProperties(form, user);

		user.setPrefectureId(prefecture);

		System.out.println("user = " + user.getPrefectureId());

		// 会員情報を保存
		userRepository.save(user);

		BeanUtils.copyProperties(user, userBean);

		session.setAttribute("user", userBean);
		return "redirect:/user/regist/complete";
	}

	/**
	 * 会員情報完了画面表示処理
	 *
	 * @param form 会員情報
	 * @return "user/regist/user_regist_complete" 会員情報 登録完了画面へ
	 */
	@RequestMapping(path = "/user/regist/complete", method = RequestMethod.GET)
	public String registComplete(Model model) {


		return "user/regist/user_regist_complete";
	}
}
