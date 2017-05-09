# test-media
#### Khác với `MediaPlayer`, `VideoView` không biếu cho bạn phương thức `setVolume` (chỉ biếu trong listenner thôi)
Vì vậy để có thể set được volume được  cần tạo một `Component` dựa trên `VideoView` bổ sung các phương thức để có thể `setVolume` 
