import 'package:flutter/material.dart';

class Auth extends ChangeNotifier {
  int id=1;
  String token='';
  bool fetching=false;


  Auth({this.id, this.token});

  factory Auth.fromJson(Map<String, dynamic> json) {
    return Auth(
      id: json['id'],
      token: json['token'],
    );
  }

  void setFetching(bool fetching){
    this.fetching = fetching;
    notifyListeners();
  }

  void changeAuth(Map<String, dynamic> json) {
    this.id = 1;
    this.token = json['token'];
    notifyListeners();
  }
}
