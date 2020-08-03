import 'package:flutterentradeapp/model/AuthModel.dart';
import 'package:flutterentradeapp/screens/login/LoginScreenModel.dart';
import 'package:flutterentradeapp/theme/theme.dart';
import 'package:flutterentradeapp/utils/common_utils.dart';
import 'package:flutterentradeapp/widget/Input.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class LoginScreen extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _LoginScreen();
  }
}

class _LoginScreen extends State<LoginScreen> {
  final userNameController = TextEditingController();
  final passwordController = TextEditingController();

  @override
  void dispose() {
    // Clean up the controller when the widget is disposed.
    userNameController.dispose();
    passwordController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(body: Consumer<Auth>(builder: (context, auth, child) {
      return auth.fetching
          ? renderFetching()
          : renderForm(context, userNameController, passwordController);
    }));
  }
}

renderFetching() {
  return new Center(
      child: Container(
    margin: new EdgeInsets.symmetric(horizontal: 36),
    child: LinearProgressIndicator(
      key: ValueKey("prgLogin"),
    ),
  ));
}

renderForm(BuildContext context, TextEditingController userNameController,
    TextEditingController passwordController) {
  final _formKey = GlobalKey<FormState>();
  final FocusNode _passwordFocusNode = FocusNode();
  final loginScreenModel = LoginScreenModel();

  return new Form(
    key: _formKey,
    child: Center(
      child: Container(
        decoration: BoxDecoration(
          image: DecorationImage(
            image: AssetImage("assets/images/bg_login.jpg"),
            fit: BoxFit.cover,
          ),
        ),
        padding: EdgeInsets.fromLTRB(32, 16, 32, 16),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Image(
                image: AssetImage("assets/images/logo.png"),
                height: 42,
                width: 158,
                fit: BoxFit.fitWidth),
            SizedBox(height: 80),
            TextFormField(
              controller: userNameController,
              validator: (value) {
                if (value.isEmpty) {
                  return 'Tên đăng nhập không hợp lệ';
                }
                if (!isEmail(value)) {
                  return 'Tên đăng nhập không hợp lệ';
                }
                return null;
              },
              key: ValueKey("inpEmail"),
              decoration: InputDecoration(
                  hintText: 'Email / Số điện thoại',
                  prefixIcon: inputDecorationIcon(Icons.person)),
              textInputAction: TextInputAction.next,
              onFieldSubmitted: (term) {
                fieldFocusChange(context, _passwordFocusNode);
              },
            ),
            SizedBox(height: 20),
            TextFormField(
              controller: passwordController,
              focusNode: _passwordFocusNode,
              key: ValueKey("inpPassword"),
              validator: (value) {
                if (value.isEmpty) {
                  return "Mật khẩu không hợp lệ";
                }
                if (value.length < 4) {
                  return 'Mật khẩu không hợp lệ';
                }
                return null;
              },
              decoration: InputDecoration(
                  hintText: 'Mật khẩu',
                  prefixIcon: inputDecorationIcon(Icons.lock_outline)),
              obscureText: true,
            ),
            SizedBox(
              height: 24,
            ),
            SizedBox(
                width: double.infinity,
                height: 48,
                child: RaisedButton(
                    key: ValueKey("btnLogin"),
                    color: appTheme.primaryColor,
                    child: Text(
                      'ĐĂNG NHẬP',
                      style: TextStyle(color: Colors.white),
                    ),
                    onPressed: () {
                      if (_formKey.currentState.validate()) {
                        String username =
                            userNameController.text; //"kimthep.it@gmail.com";
                        String password = passwordController.text; //"123456";

                        loginScreenModel.login(context, username, password);
                      }
                    }))
          ],
        ),
      ),
    ),
  );
}
