import 'package:entrade/theme/theme.dart';
import 'package:entrade/utils/common_utils.dart';
import 'package:entrade/widget/Input.dart';
import 'package:flutter/material.dart';

class LoginScreen extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _LoginScreen();
  }
}

class _LoginScreen extends State<LoginScreen> {
  final _formKey = GlobalKey<FormState>();
  final FocusNode _passwordFocusNode = FocusNode();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Form(
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
                  validator: (value) {
                    if (value.isEmpty) {
                      return 'Tên đăng nhập không hợp lệ';
                    }
                    if (!isEmail(value)) {
                      return 'Tên đăng nhập không hợp lệ';
                    }
                    return null;
                  },
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
                  validator: (value) {
                    if (value.isEmpty) {
                      return "Mật khẩu không hợp lệ";
                    }
                    if (value.length < 4) {
                      return 'Mật khẩu không hợp lệ';
                    }
                    return null;
                  },
                  focusNode: _passwordFocusNode,
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
                      color: appTheme.primaryColor,
                      child: Text('ĐĂNG NHẬP',style: TextStyle(color: Colors.white),),
                      onPressed: () {
                        if (_formKey.currentState.validate()) {
                          Navigator.pushReplacementNamed(context, '/catalog');
                        }
                      },
                    ))
              ],
            ),
          ),
        ),
      ),
    );
  }
}
