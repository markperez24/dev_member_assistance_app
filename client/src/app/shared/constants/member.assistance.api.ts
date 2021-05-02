export declare const enum MemberAssistanceApi {
  //PUBLIC
  POST_REGISTER_USER ='/public/register/',
  POST_VERIFY_CAPTCHA = '/public/verify-captcha',
  GET_EMAIL_EXISTS ='/public/email-exists/',
  GET_INIT_VERIFICATION = '/public/verification/',
  //REPORTS
  GET_REPORTS_MEMBERS = '/api/reports/get-members',
  POST_MEMBER_GENERATE_REPORTS = '/api/reports/generate-member-reports/',
  GET_MEMBER_GENERATE_REPORTS = '/api/reports/download-member-reports/',
  GET_REPORTS_HOSPITALS = '/api/reports/get-hospitals',
  POST_HOSPITAL_GENERATE_REPORTS = '/api/reports/generate-hospital-reports/',
  GET_HOSPITAL_GENERATE_REPORTS = '/api/reports/download-hospital-reports/',
  //HOSPITAL
  POST_REGISTER_HOSPITAL = '/api/admin/hospital/register',
  GET_VIDEO_DETAILS = '/public/get-video-details',
  //ADMIN
  GET_HOSPITALS = '/api/admin/hospital/list',
  GET_HOSPITAL_DETAILS = '/api/admin/hospital/get-hospital-details',
  GET_HOSPITAL_MEDICAL_ASSISTANCE_DETAILS = '/api/admin/hospital/view-by-hospital',
  GET_HOSPITAL_BUDGET_HISTORY = '/api/admin/hospital/view-budget-history',
  POST_SAVE_HOSPITAL_ADDITIONAL_BUDGET = '/api/admin/hospital/add-budget',
  GET_MEMBERS = '/api/admin/get-member-list',
  GET_MEMBER_DETAILS = '/api/admin/get-member-details',
  GET_PROFILE_PHOTO_BY_ACCOUNT_NUMBER = '/api/admin/get-profile-photo-by-account-number/',
  GET_MEMBER_VOUCHER_IMAGE= '/api/admin/get-voucher-image/',
  POST_DENY_APPLICATION = '/api/admin/deny-application/',
  POST_APPROVE_APPLICATION = '/api/admin/approve-application/',
  GET_APPLICATION = '/api/admin/get-medical-application',
//MEMBER
  POST_UPLOAD_PROFILE_PHOTO = '/api/member/upload-profile-photo',
  POST_UPLOAD_ID_PHOTO = '/api/member/upload-id-photo',
  GET_MY_PROFILE = '/api/member/my-profile',
  POST_UPDATE_MY_PROFILE = '/api/member/update-my-profile/',
  POST_MEMBER_CHANGE_PASSWORD = '/api/member/change-password/',
  GET_PROFILE_PHOTO = '/api/member/get-profile-photo',
  GET_ID_PHOTO = '/api/member/get-id-photo/',
  GET_MEDICAL_ASSISTANCE='/api/medical-assistance/view',
  GET_MEDICAL_ASSISTANCE_VOUCHER_IMAGE = '/api/medical-assistance/get-voucher-image/',
  GET_APPLY_MEDICAL_ASSISTANCE='/api/medical-assistance/apply',
  POST_SAVE_MEDICAL_ASSISTANCE_APPLICATION = '/api/medical-assistance/save/',
  POST_UPDATE_MEDICAL_ASSISTANCE_APPLICATION = '/api/medical-assistance/claim-voucher/'
}
