package jp.co.sss.shop.controller.user;

import java.sql.Date;

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
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.FeeRepository;
import jp.co.sss.shop.repository.UserRepository;

@Controller
public class UserUpdateCustomerController {
	/**
	 * 会員情報
	 */
	@Autowired
	UserRepository userRepository;

	@Autowired
	FeeRepository feeRepository;

	/**
	 * セッション
	 */
	@Autowired
	HttpSession session;

	/**
	 * 会員情報の変更入力画面表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @param form  会員情報フォーム
	 * @return "user/update/user_update_input" 会員情報 変更入力画面へ
	 **/
	@RequestMapping(path = "/user/update/input", method = RequestMethod.POST)
	public String updateCustomerInput(boolean backFlg, Model model, @ModelAttribute UserForm form) {

		//ドロップダウン

//		model.addAttribute("prefecture",feeRepository.findAll());


		// 戻るボタンかどうかを判定
		if (!backFlg) {

			// 変更対象の会員情報を取得
			User user = userRepository.getOne(form.getId());
			UserBean userBean = new UserBean();

			// Userエンティティの各フィールドの値をUserBeanにコピー
			BeanUtils.copyProperties(user, userBean);

			// 会員情報をViewに渡す
			model.addAttribute("user", userBean);

		} else {

			UserBean userBean = new UserBean();
			// 入力値を会員情報にコピー
			BeanUtils.copyProperties(form, userBean);

			// 会員情報をViewに渡す
			model.addAttribute("user", userBean);

		}
		return "user/update/user_update_input";
	}

	/**
	 * 会員情報 変更確認処理
	 *
	 * @param model  Viewとの値受渡し
	 * @param form   会員情報フォーム
	 * @param result 入力チェック結果
	 * @return
	 * 入力値エラーあり："user/update/user_update_input" 会員情報変更入力画面へ
	 * 入力値エラーなし："user/update/user_update_check" 会員情報 変更確認画面へ
	 */
	@RequestMapping(path = "/user/update/check", method = RequestMethod.POST)
	public String updateCustomerCheck( Model model, @Valid @ModelAttribute UserForm form, BindingResult result) {
		// 入力値にエラーがあった場合、会員情報 変更入力画面表示処理に戻る
		if (result.hasErrors()) {

			UserBean userBean = new UserBean();
			// 入力値を会員情報にコピー
			BeanUtils.copyProperties(form, userBean);

			// 会員情報をViewに渡す
			model.addAttribute("user", userBean);

			return "user/update/user_update_input";
		}

		return "user/update/user_update_check";
	}

	/**
	 * 会員情報変更完了処理
	 *
	 * @param model Viewとの値受渡し
	 * @param form  会員情報
	 * @return "user/update/user_update_complete" 会員情報 変更完了画面へ
	 */
	@RequestMapping(path = "/user/update/complete", method = RequestMethod.POST)
	public String updateCustomerComplete(Model model, @ModelAttribute UserForm form) {

		// 変更対象の会員情報を取得
		User user = userRepository.findById(form.getId()).orElse(null);

		// 会員情報の削除フラグを取得
		Integer deleteFlag = user.getDeleteFlag();
		// 会員情報の登録日付を取得
		Date insertDate = user.getInsertDate();

		// 入力値をUserエンティティの各フィールドにコピー
		BeanUtils.copyProperties(form, user);

		// 削除フラグをセット
		user.setDeleteFlag(deleteFlag);
		// 登録日付をセット
		user.setInsertDate(insertDate);

//		// 会員情報を保存
		userRepository.save(user);

		// セッションからログインユーザーの情報を取得
		UserBean userBean = (UserBean) session.getAttribute("user");
		// 変更対象の会員が、ログインユーザと一致していた場合セッション情報を変更
		if (user.getId().equals(userBean.getId())) {
			// Userエンティティの各フィールドの値をUserBeanにコピー
			BeanUtils.copyProperties(form, userBean);
			// 会員情報をViewに渡す
			session.setAttribute("user", userBean);
		}
		return "redirect:/user/update/complete";
	}

	/**
	 * 会員情報変更完了画面表示
	 *
	 * @return "user/update/user_update_complete" 会員情報 変更完了画面へ
	 */
	@RequestMapping(path = "/user/update/complete", method = RequestMethod.GET)
	public String updateCustomerCompleteRedirect() {
		return "user/update/user_update_complete";
	}
}
