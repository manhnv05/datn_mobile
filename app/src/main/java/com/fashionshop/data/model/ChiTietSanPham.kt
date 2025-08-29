package com.fashionshop.data.model


class ChiTietSanPham (
var id: Int,
var sanPham: SanPham,
var chatLieu: ChatLieu,
var thuongHieu: ThuongHieu,
var mauSac: MauSac,
var kichThuoc: KichThuoc,
var coAo: CoAo,
var tayAo: TayAo,
var maSanPhamChiTiet: String,
var gia: Int,
var soLuong: Int,
var trongLuong: Int,
var trangThai: Int,
var spctHinhAnhs: List<SpctHinhAnh>
)