package com.fashionshop.data.model

class SanPham(
    var id: Int,
    var xuatXu: String,
    var danhMuc: DanhMuc,
    var maSanPham: String,
    var tenSanPham: String,
    var trangThai: Int,
    var moTa: String,
    var chiTietSanPhams: List<ChiTietSanPham>,

)