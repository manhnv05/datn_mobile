package com.fashionshop.data.model

class DanhMuc (
    var id: Int,
    var maDanhMuc: String,
    var tenDanhMuc: String,
    var trangThai: Int,
    var sanPhams: List<SanPham>
)