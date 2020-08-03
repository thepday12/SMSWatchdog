import 'dart:convert';

import 'package:http/http.dart' as http;

Future<http.Response> requestLogin(String username, String password) async {
  return http.post(
      "https://fdc-services-uat.entrade.com.vn/entrade-api/v2/auth",
      body: jsonEncode(<String, String>{
        'username': username,
        'password': password,
      }));
}
