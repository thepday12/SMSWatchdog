import 'package:flutter_driver/flutter_driver.dart';
import 'package:test/test.dart';

void main() {
  group('Login App', () {
    // First, define the Finders and use them to locate widgets from the
    // test suite. Note: the Strings provided to the `byValueKey` method must
    // be the same as the Strings we used for the Keys in step 1.
    final inpEmail = find.byValueKey("inpEmail");
    final inpPassword = find.byValueKey('inpPassword');
    final btnLogin = find.byValueKey('btnLogin');

    FlutterDriver driver;

    // Connect to the Flutter driver before running any tests.
    setUpAll(() async {
      driver = await FlutterDriver.connect();
    });

    // Close the connection to the driver after the tests have completed.
    tearDownAll(() async {
      if (driver != null) {
        driver.close();
      }
    });

    isPresent(SerializableFinder serializableFinder, FlutterDriver driver, {Duration timeout = const Duration(seconds: 30)}) async {
      try {
        await driver.waitFor(serializableFinder,timeout: timeout);
        return true;
      } catch(exception) {
        return false;
      }
    }

    test('test login success', () async {
      await driver.tap(inpEmail);
      await driver.enterText('kimthep.it@gmail.com');
      await new Future.delayed(new Duration(seconds: 2));
      await driver.tap(inpPassword);
      await driver.enterText('123456');
      await new Future.delayed(new Duration(seconds: 2));
      await driver.tap(btnLogin);
      final loginSuccess = await isPresent(find.text('Welcome'),driver);
      expect(loginSuccess, true);
    });
  });
}