# 📇 Business Card Scanner App

An advanced Android app that lets users **scan business cards**, **edit extracted details**, and **save them to Firebase** or **locally with Room**. Built with **Google Sign-In**, **ML Kit OCR**, **Koin DI**, and **MVVM architecture**.

---

## ✨ Features

- 📸 **Capture Business Card:** Take a picture using the device camera.
- 🧠 **OCR with ML Kit:** Extract Name, Phone, Email, Company, and Address using Google ML Kit.
- ✏️ **Editable Fields:** Users can modify the extracted fields before saving.
- 🔐 **Google Authentication:** Secure login via Google account.
- ☁️ **Save to Firebase:** Save business card data directly to Firebase Realtime Database.
- ⚙️ **Koin DI:** Lightweight dependency injection using Koin.

---

## 🎬 App Flow

1. User **signs in** with Google  
2. Clicks **Capture** to scan a business card  
3. ML Kit **extracts text**  
4. Fields are **auto-filled and editable**  
5. User clicks **Save Card**  
6. Card is saved to **Firebase**  
7. Cards are available on next login

---

## 🧩 Tech Stack

| Layer         | Technology Used                          |
|---------------|------------------------------------------|
| UI            | XML + Material Components                |
| OCR           | ML Kit Text Recognition                  |
| Auth          | Firebase Google Sign-In                  |
| Remote DB     | Firebase Realtime Database               |     
| DI            | Koin                                     |
| Architecture  | MVVM (ViewModel + Repository)            |
| Image Capture | Android Camera Intent                    |

---

## 📱 APK Download

🔗 [https://www.mediafire.com/file/k63dagxs3ve8p0t/app-debug.apk/file]

---

## 🛠 How to Run Locally

1. Clone the repository  
   ```bash
   git clone https://github.com/your-username/business-card-app.git

   
APk DEMO Video 
https://github.com/user-attachments/assets/0af84aad-04aa-40b3-8bd6-c23537995e87

Firebase Data store 
![Screenshot (909)](https://github.com/user-attachments/assets/72031b07-63fb-4c79-9564-d6d8548e3f77)
![Screenshot (908)](https://github.com/user-attachments/assets/46a17c18-277b-4ffd-a74c-e54f7a12e7f2)



