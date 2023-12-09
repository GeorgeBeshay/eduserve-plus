export class Admin{

  adminId: string
  adminPwHash: string
  adminName: string
  creatorAdminId: string

  constructor(adminId: string, adminPwHash: string, adminName: string, creatorAdminId: string) {
    this.adminId = adminId
    this.adminName = adminName
    this.adminPwHash = adminPwHash
    this.creatorAdminId = creatorAdminId
  }
}
