const int redPin = 13;

void setup() {
  Serial.begin(9600);
  pinMode(redPin, OUTPUT);
}

void loop() {
  while (Serial.available() > 0) {
    float a = Serial.parseFloat();
    float b = Serial.parseFloat();
    float c = Serial.parseFloat();
    int d = Serial.parseInt();

    analogWrite(redPin, d);

    Serial.print(a);
    Serial.print(" ");
    Serial.print(b);
    Serial.print(" ");
    Serial.print(c);
    Serial.print(" ");
    Serial.println(d);
  }

}
