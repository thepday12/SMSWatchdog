import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutterentradeapp/model/AuthModel.dart';
import 'package:flutterentradeapp/network/Auth.dart';
import 'package:http/http.dart' as http;
import 'package:provider/provider.dart';

class LoginScreenModel {
  login(
    BuildContext context,
    String username,
    String password,
  ) {
    final auth = Provider.of<Auth>(context, listen: false);
    auth.setFetching(true);
    return requestLogin(username, password)
        .then((response) =>
            {handleLoginSuccess(context, response), auth.setFetching(false)})
        .catchError((onError) => auth.setFetching(false));
  }

  handleLoginSuccess(BuildContext context, http.Response response) {
    if (response.statusCode == 200) {
      Provider.of<Auth>(context, listen: false)
          .changeAuth(json.decode(response.body));
      Navigator.pushReplacementNamed(context, '/welcome');
    }
  }
}
