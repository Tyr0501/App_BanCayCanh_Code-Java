package com.example.asm_java_nangcao1.Activity;import androidx.annotation.NonNull;import androidx.appcompat.app.AppCompatActivity;import android.annotation.SuppressLint;import android.app.Activity;import android.app.Dialog;import android.content.Intent;import android.content.SharedPreferences;import android.graphics.Color;import android.graphics.drawable.ColorDrawable;import android.net.Uri;import android.os.Bundle;import android.util.Log;import android.view.Gravity;import android.view.View;import android.view.Window;import android.view.WindowManager;import android.widget.EditText;import android.widget.ImageView;import android.widget.LinearLayout;import android.widget.TextView;import android.widget.Toast;import com.bumptech.glide.Glide;import com.example.asm_java_nangcao1.R;import com.example.asm_java_nangcao1.Model.khoHang_Model;import com.example.asm_java_nangcao1.service.TaskService;import com.google.android.gms.tasks.OnFailureListener;import com.google.android.gms.tasks.OnSuccessListener;import com.google.firebase.database.DatabaseReference;import com.google.firebase.database.FirebaseDatabase;import com.google.firebase.storage.FirebaseStorage;import com.google.firebase.storage.StorageReference;import com.google.firebase.storage.UploadTask;import java.net.URL;import java.text.DateFormat;import java.text.ParseException;import java.text.SimpleDateFormat;import java.util.Date;import java.util.UUID;import java.util.regex.Matcher;import java.util.regex.Pattern;public class suaSanPham_themSanPham_adm_Activity extends AppCompatActivity {    private ImageView            ThemSuaXoaSanPham_img_btn_back,            ThemSuaXoaSanPham_img_showImgV,            ThemSuaXoaSanPham_img_deleteImg;    private TextView            ThemSuaXoaSanPham_tv_btn_moThuMucAnh;    private EditText            ThemSuaXoaSanPham_edt_tenSanPham,            ThemSuaXoaSanPham_edt_giaSanPham,            ThemSuaXoaSanPham_edt_xuatXuSanPham,            ThemSuaXoaSanPham_edt_loaiSanPham,            ThemSuaXoaSanPham_edt_ngayNhapSanPham;    private String tenSanPham;    private String giaSanPham;    private String xuatXuSanPham;    private String loaiSanPham;    private String ngayNhap;    private LinearLayout            ThemSuaXoaSanPham_llout_btn_xoaSanPham,            ThemSuaXoaSanPham_llout_btn_suaSanPham,            ThemSuaXoaSanPham_llout_btn_themSanPham;    //    Firebase    private FirebaseDatabase database;    private DatabaseReference myRef;    private FirebaseStorage storage;    private UploadTask uploadTask;    private StorageReference mountainImagesRef, storageRef;    private UUID uuid;    // Date    private Date date;    private DateFormat dateFormat;    Intent service;    //    link ảnh null    private String uriDemoImg = "https://firebasestorage.googleapis.com/v0/b/asmandroid-duan1.appspot.com/o/ic_img.png?alt=media&token=d73c4b41-000f-4d30-abcb-3de1e1a66ab2";    // final    final static String DATE_FORMAT = "dd-MM-yyyy";    //    Model    private khoHang_Model listSanPham;    private static final int REQUEST_IMAGE_OPEN = 123;    Uri urlImg, full,full2;    String idSanPhamit, anhSanPhamit, tenSanPhamit, giaSanPhamit, xuatXuit, ngayNhapit;    int loaiSanPhamit;SharedPreferences checkTask;    @Override    public void onDestroy() {        super.onDestroy();    }    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_sua_san_pham_them_san_pham_adm);//     ánh xạ id bên activity        anhXa();        //     gán giá trị trước        setValue();        // kieerm tra intent        nhanDuLieuIntent();        if (idSanPhamit != null) {            ThemSuaXoaSanPham_llout_btn_themSanPham.setVisibility(View.INVISIBLE);            ThemSuaXoaSanPham_llout_btn_xoaSanPham.setVisibility(View.VISIBLE);            ThemSuaXoaSanPham_llout_btn_suaSanPham.setVisibility(View.VISIBLE);            setValueIntent();        } else {            ThemSuaXoaSanPham_llout_btn_themSanPham.setVisibility(View.VISIBLE);            ThemSuaXoaSanPham_llout_btn_xoaSanPham.setVisibility(View.INVISIBLE);            ThemSuaXoaSanPham_llout_btn_suaSanPham.setVisibility(View.INVISIBLE);        }//     bắt sự kiện khi thao tác        batSuKien();    }    /********Lưu lên Firebase  Reatime*********/    private void themSanPhamVaoFireBaseKhoHang() {        anhXa();        getValue();        if (!checkValue()){            return;        }        openDialogAddProduc();    }    /********Lưu lên Firebase  Reatime*********/    private void themSanPhamVaoFireBaseKhoHangCoAnh() {        anhXa();        getValue();        if (!checkValue()){            return;        }        openDialogAddProducImg();    }    private void batSuKien() {        anhXa();        /********bắt sự xóa*********/        ThemSuaXoaSanPham_llout_btn_xoaSanPham.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                openDialogDelete();            }        });        /********bắt sự sửa*********/        ThemSuaXoaSanPham_llout_btn_suaSanPham.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                openDialogUpdate();            }        });        /********bắt sự kiện thêm*********/        ThemSuaXoaSanPham_llout_btn_themSanPham.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                if (!(full == null)) {//                    nếu như có ảnh được chọn                    themSanPhamVaoFireBaseKhoHangCoAnh();                    Log.d("img", full + "");                } else {//                    nếu như không có ảnh được chọn                    themSanPhamVaoFireBaseKhoHang();                }            }        });        /********bắt sự kiện mở file ảnh*********/        ThemSuaXoaSanPham_tv_btn_moThuMucAnh.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                moFileThuMucAnh();            }        });        /********bắt sự kiện xóa ảnh*********/        ThemSuaXoaSanPham_img_deleteImg.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                full = null;                ThemSuaXoaSanPham_img_showImgV.setImageURI(full);                ThemSuaXoaSanPham_img_showImgV.setImageResource(R.drawable.ic_img);                ThemSuaXoaSanPham_img_deleteImg.setVisibility(View.INVISIBLE);            }        });        /********bắt sự kiện thoát form*********/        ThemSuaXoaSanPham_img_btn_back.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                Intent intent = new Intent(suaSanPham_themSanPham_adm_Activity.this, quanLyKho_Activity.class);                startActivity(intent);            }        });    }    /********bắt sự kiện sửa sản phẩm*********/    private void suaSanPhamKho() {        if (!checkValue()){            return;        }        myRef = database.getReference("khoHang");        int loaiSanPhams = Integer.parseInt(loaiSanPham);        myRef.child(idSanPhamit).child("tenSanPham").setValue(tenSanPham);        myRef.child(idSanPhamit).child("giaSanPham").setValue(giaSanPham);        myRef.child(idSanPhamit).child("loaiSanPham").setValue(loaiSanPhams);        myRef.child(idSanPhamit).child("xuatXu").setValue(xuatXuSanPham);        myRef.child(idSanPhamit).child("ngayNhap").setValue(ngayNhap);        Intent intent = new Intent(suaSanPham_themSanPham_adm_Activity.this, quanLyKho_Activity.class);        startActivity(intent);    }    /********bắt sự kiện xóa sản phẩm*********/    private void xoaSanPhamKho() {        myRef = database.getReference("khoHang");        myRef.child(idSanPhamit).removeValue();        Intent intent = new Intent(suaSanPham_themSanPham_adm_Activity.this, quanLyKho_Activity.class);        startActivity(intent);    }    private void nhanDuLieuIntent() {        Intent intent = getIntent();        Bundle bundle = intent.getExtras();        if (bundle != null) {            idSanPhamit = bundle.getString("key_idSanPham", "");            anhSanPhamit = bundle.getString("key_imgSanPham", "");            tenSanPhamit = bundle.getString("key_tenSanPham", "");            giaSanPhamit = bundle.getString("key_giaSanPham", "");            xuatXuit = bundle.getString("key_xuatXuSanPham", "");            ngayNhapit = bundle.getString("key_ngayNhapSanPham", "");            loaiSanPhamit = bundle.getInt("key_loaiSanPham", 0);        }    }    /********CheckDate*********/    public static boolean isDateValid(String date) {        boolean status = false;        if (checkDate(date)) {            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");            dateFormat.setLenient(false);            try {                dateFormat.parse(date);                status = true;            } catch (Exception e) {                status = false;            }        }        return status;    }    static boolean checkDate(String date) {        String pattern = "(0?[1-9]|[12][0-9]|3[01])\\/(0?[1-9]|1[0-2])\\/([0-9]{4})";        boolean flag = false;        if (date.matches(pattern)) {            flag = true;        }        return flag;    }    /********bắt sự kiện nhấn mở thư việnh*********/    private void moFileThuMucAnh() {        anhXa();        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);        intent.setType("image/*");        intent.addCategory(Intent.CATEGORY_OPENABLE);        startActivityForResult(intent, REQUEST_IMAGE_OPEN);    }    /********Lưu ảnh lên storage nếu có ảnh và lưu ngược lại vào Reatime*********/    private void luuAnhLenStorageKhoHang() {        storageRef = storage.getReference();        mountainImagesRef = storageRef.child("images/sanPham/" + new Date().getTime() + ".jpg");        uploadTask = mountainImagesRef.putFile(full);        //  kiếm tra        //  nếu lỗi file chạy vào đây        uploadTask.addOnFailureListener(new OnFailureListener() {            @Override            public void onFailure(Exception e) {                Toast.makeText(suaSanPham_themSanPham_adm_Activity.this, "Lưu ảnh thất bại", Toast.LENGTH_SHORT).show();            }            //     add file thành công        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {            @Override            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {                //    khởi tạo link url                mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {                    @Override                    public void onSuccess(Uri uri) {                        if (!checkValue()){                            return;                        }                        checkValue();//                        //      myRef chỉ con trỏ tại vị trí ""                        myRef = database.getReference("khoHang");////                        //      kiểm tra điều kiện nếu như có ảnh trên storage thì lưu có định dạng không thì rỗng                        listSanPham = new khoHang_Model(uuid.toString(), tenSanPham, giaSanPham, Integer.parseInt(loaiSanPham), xuatXuSanPham + "", ngayNhap + "", uri.toString() + "");////                        //        add giá trị                        myRef.child(listSanPham.getIdSanPham().toString()).setValue(listSanPham);                        Toast.makeText(suaSanPham_themSanPham_adm_Activity.this, "Lưu ảnh thành công!!!", Toast.LENGTH_SHORT).show();                    }                });            }        });    }    // lấy ảnh trong thư viện và show anh lên imgview    @Override    protected void onActivityResult(int requestCode, int resultCode, Intent data) {        //  bắt sự kiến khi nhấn lưu ảnh thỉ gán vào imgview        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK && data != null && data.getData() != null) {            full = data.getData();            ThemSuaXoaSanPham_img_showImgV.setImageURI(full);            ThemSuaXoaSanPham_img_deleteImg.setVisibility(View.VISIBLE);        }        super.onActivityResult(requestCode, resultCode, data);    }    /********Lưu ảnh lên storage nếu có ảnh và lưu ngược lại vào Reatime*********/    private void luuAnhUptate() {        storageRef = storage.getReference();        mountainImagesRef = storageRef.child("images/sanPham/" + new Date().getTime() + ".jpg");        uploadTask = mountainImagesRef.putFile(full);        //  kiếm tra        //  nếu lỗi file chạy vào đây        uploadTask.addOnFailureListener(new OnFailureListener() {            @Override            public void onFailure(Exception e) {                Toast.makeText(suaSanPham_themSanPham_adm_Activity.this, "Lưu ảnh thất bại", Toast.LENGTH_SHORT).show();            }            //     add file thành công        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {            @Override            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {                //    khởi tạo link url                mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {                    @Override                    public void onSuccess(Uri uri) {                        if (!checkValue()){                            return;                        }                        myRef = database.getReference("khoHang");                        int loaiSanPhams = Integer.parseInt(loaiSanPham);                        myRef.child(idSanPhamit).child("tenSanPham").setValue(tenSanPham);                        myRef.child(idSanPhamit).child("giaSanPham").setValue(giaSanPham);                        myRef.child(idSanPhamit).child("loaiSanPham").setValue(loaiSanPhams);                        myRef.child(idSanPhamit).child("xuatXu").setValue(xuatXuSanPham);                        myRef.child(idSanPhamit).child("ngayNhap").setValue(ngayNhap);                        myRef.child(idSanPhamit).child("hinhAnh").setValue(uri.toString() + "");                        Intent intent = new Intent(suaSanPham_themSanPham_adm_Activity.this, quanLyKho_Activity.class);                        startActivity(intent);                        Toast.makeText(suaSanPham_themSanPham_adm_Activity.this, "Lưu thành công!!!", Toast.LENGTH_SHORT).show();                    }                });            }        });    }    private void openDialogAddProduc() {        final Dialog dialog = new Dialog(this);        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);        dialog.setContentView(R.layout.dialog_cuttom_themsanpham);        Window window = dialog.getWindow();        if (window == null) {            return;        }        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));        WindowManager.LayoutParams windowAttributes = window.getAttributes();        windowAttributes.gravity = Gravity.CENTER;        window.setAttributes(windowAttributes);//        sử lý khi nhấn ra ngoài thì có thoát hay không        dialog.setCancelable(false);//        khai báo bắt sự kiện        TextView Dialog_them_tvBtn_khong = dialog.findViewById(R.id.dialog_them_tvBtn_khong);        TextView Dialog_them_tvBtn_dongY = dialog.findViewById(R.id.dialog_them_tvBtn_dongY);//        tắt dialog đi        Dialog_them_tvBtn_khong.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                dialog.dismiss();            }        });        Dialog_them_tvBtn_dongY.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                SharedPreferences.Editor editor = checkTask.edit();                editor.putString("nameTask",tenSanPham);                editor.apply();                dialog.dismiss();                addValue_Firebase();                Intent intent = new Intent(suaSanPham_themSanPham_adm_Activity.this,quanLyKho_Activity.class);                startActivity(intent);            }        });        dialog.show();    }    private void openDialogAddProducImg() {        final Dialog dialog = new Dialog(this);        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);        dialog.setContentView(R.layout.dialog_cuttom_themsanpham);        Window window = dialog.getWindow();        if (window == null) {            return;        }        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));        WindowManager.LayoutParams windowAttributes = window.getAttributes();        windowAttributes.gravity = Gravity.CENTER;        window.setAttributes(windowAttributes);//        sử lý khi nhấn ra ngoài thì có thoát hay không        dialog.setCancelable(false);//        khai báo bắt sự kiện        TextView Dialog_them_tvBtn_khong = dialog.findViewById(R.id.dialog_them_tvBtn_khong);        TextView Dialog_them_tvBtn_dongY = dialog.findViewById(R.id.dialog_them_tvBtn_dongY);//        tắt dialog đi        Dialog_them_tvBtn_khong.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                dialog.dismiss();            }        });        Dialog_them_tvBtn_dongY.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                SharedPreferences.Editor editor = checkTask.edit();                editor.putString("nameTask",tenSanPham);                editor.apply();                dialog.dismiss();                luuAnhLenStorageKhoHang();                Intent intent = new Intent(suaSanPham_themSanPham_adm_Activity.this,quanLyKho_Activity.class);                startActivity(intent);            }        });        dialog.show();    }    private void openDialogDelete() {        final Dialog dialog = new Dialog(this);        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);        dialog.setContentView(R.layout.dialog_cuttom_xoasanpham);        Window window = dialog.getWindow();        if (window == null) {            return;        }        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));        WindowManager.LayoutParams windowAttributes = window.getAttributes();        windowAttributes.gravity = Gravity.CENTER;        window.setAttributes(windowAttributes);//        sử lý khi nhấn ra ngoài thì có thoát hay không        dialog.setCancelable(false);//        khai báo bắt sự kiện        TextView Dialog_them_tvBtn_khong = dialog.findViewById(R.id.dialog_them_tvBtn_khong);        TextView Dialog_them_tvBtn_dongY = dialog.findViewById(R.id.dialog_them_tvBtn_dongY);//        tắt dialog đi        Dialog_them_tvBtn_khong.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                dialog.dismiss();            }        });        Dialog_them_tvBtn_dongY.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                dialog.dismiss();                xoaSanPhamKho();            }        });        dialog.show();    }    private void openDialogUpdate() {        final Dialog dialog = new Dialog(this);        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);        dialog.setContentView(R.layout.dialog_cuttom_capnhatsanpham);        Window window = dialog.getWindow();        if (window == null) {            return;        }        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));        WindowManager.LayoutParams windowAttributes = window.getAttributes();        windowAttributes.gravity = Gravity.CENTER;        window.setAttributes(windowAttributes);//        sử lý khi nhấn ra ngoài thì có thoát hay không        dialog.setCancelable(false);//        khai báo bắt sự kiện        TextView Dialog_them_tvBtn_khong = dialog.findViewById(R.id.dialog_them_tvBtn_khong);        TextView Dialog_them_tvBtn_dongY = dialog.findViewById(R.id.dialog_them_tvBtn_dongY);//        tắt dialog đi        Dialog_them_tvBtn_khong.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                dialog.dismiss();            }        });        Dialog_them_tvBtn_dongY.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                dialog.dismiss();                if (!(full == null)) {//                    nếu như có ảnh được chọn                    luuAnhUptate();                } else {//                    nếu như không có ảnh được chọn                    suaSanPhamKho();                }            }        });        dialog.show();    }    private void addValue_Firebase() {        //   myRef chỉ con trỏ tại vị trí ""        myRef = database.getReference("khoHang");        //  kiểm tra điều kiện nếu như có ảnh trên storage thì lưu có định dạng không thì rỗng        listSanPham = new khoHang_Model(uuid.toString(), tenSanPham, giaSanPham, Integer.parseInt(loaiSanPham), xuatXuSanPham + "", ngayNhap + "", uriDemoImg);        //  add giá trị        myRef.child(listSanPham.getIdSanPham().toString()).setValue(listSanPham);        service = new Intent(this, TaskService.class);        startService(service);    }    private void getValue() {        String pattern = "MM/dd/yyyy HH:mm:ss";        uuid = UUID.randomUUID();        //     setEditText        tenSanPham = ThemSuaXoaSanPham_edt_tenSanPham.getText().toString().trim();        giaSanPham = ThemSuaXoaSanPham_edt_giaSanPham.getText().toString();        xuatXuSanPham = ThemSuaXoaSanPham_edt_xuatXuSanPham.getText().toString();        loaiSanPham = ThemSuaXoaSanPham_edt_loaiSanPham.getText().toString();        ngayNhap = pattern.format(ThemSuaXoaSanPham_edt_ngayNhapSanPham.getText().toString());    }    private void setValue() {        ThemSuaXoaSanPham_edt_loaiSanPham.setText("1");        ThemSuaXoaSanPham_edt_ngayNhapSanPham.setText(dateFormat.format(date));    }    private void setValueIntent() {        Glide.with(suaSanPham_themSanPham_adm_Activity.this)                .load(anhSanPhamit)                .into(ThemSuaXoaSanPham_img_showImgV);        ThemSuaXoaSanPham_img_deleteImg.setVisibility(View.VISIBLE);        ThemSuaXoaSanPham_edt_tenSanPham.setText(tenSanPhamit);        ThemSuaXoaSanPham_edt_giaSanPham.setText(giaSanPhamit);        ThemSuaXoaSanPham_edt_xuatXuSanPham.setText(xuatXuit);        ThemSuaXoaSanPham_edt_ngayNhapSanPham.setText(ngayNhapit);        ThemSuaXoaSanPham_edt_loaiSanPham.setText(loaiSanPhamit + "");    }    private boolean checkValue() {        getValue();        if (!tenSanPham.equals("")) {            if (!(tenSanPham.length() < 5)) {                if (!giaSanPham.equals("")) {                    try { if (!(Double.parseDouble(giaSanPham.toString()) < 1)) {                            if (!xuatXuSanPham.equals("")) {                                try {                                    if (!(Integer.parseInt(loaiSanPham) < 0 || Integer.parseInt(loaiSanPham) > 3)) {                                        if (!isDateValid(ngayNhap) == false) {                                            return true;                                        } else {                                            showError(ThemSuaXoaSanPham_edt_ngayNhapSanPham, "Ngày nhập phải đúng định dạng dd/MM/yyyy !!!");                                            return false;                                        }                                    } else {                                        showError(ThemSuaXoaSanPham_edt_loaiSanPham, "Loại sản phẩm không được nhỏ hơn 0 và lớp hơn 3 !!!");                                        return false;                                    } } catch (Exception e) {                                    showError(ThemSuaXoaSanPham_edt_loaiSanPham, "Loại sản phẩm chỉ được định dạng số 1 đến 3!!!");                                    return false;                                }                            } else {                                showError(ThemSuaXoaSanPham_edt_xuatXuSanPham, "Xuất xứ sản phẩm không được để trống!!!");                                return false;                            }                        } else {                            showError(ThemSuaXoaSanPham_edt_giaSanPham, "Giá sản phẩm không được nhỏ hơn 1  !!!");                            return false;                        }                    } catch (Exception e) {                        showError(ThemSuaXoaSanPham_edt_giaSanPham, "Giá sản phẩm không được có kí tự chữ !!!");                        return false;                    }                } else {                    showError(ThemSuaXoaSanPham_edt_giaSanPham, "Giá sản phẩm không được để trống!!!");                    return false;                }            } else {                showError(ThemSuaXoaSanPham_edt_tenSanPham, "Tên sản phẩm không được nhỏ hơn 5 kí tự!!!");                return false;            }        } else {            showError(ThemSuaXoaSanPham_edt_tenSanPham, "Tên sản phẩm không được để trống!!!");            return false;        }    }    @SuppressLint("ResourceAsColor")    public void showError(EditText filed, String text) {        filed.setError(text);        filed.requestFocus();        filed.setBackgroundResource(R.drawable.broder_stroke_red_error);    }    private void anhXa() {        //    Date hiện tại        dateFormat = new SimpleDateFormat("dd/MM/yyyy");        date = new Date();        checkTask = getSharedPreferences("checkTaskService", MODE_PRIVATE);        //    Firebase        database = FirebaseDatabase.getInstance("https://asmandroid-duan1-default-rtdb.asia-southeast1.firebasedatabase.app/");        //    FirebaseStorage        storage = FirebaseStorage.getInstance("gs://asmandroid-duan1.appspot.com");        //    ImgView        ThemSuaXoaSanPham_img_btn_back = findViewById(R.id.themSuaXoaSanPham_img_btn_back);        ThemSuaXoaSanPham_img_showImgV = findViewById(R.id.themSuaXoaSanPham_img_showImgV);        ThemSuaXoaSanPham_img_deleteImg = findViewById(R.id.themSuaXoaSanPham_img_deleteImg);        //    TextView        ThemSuaXoaSanPham_tv_btn_moThuMucAnh = findViewById(R.id.themSuaXoaSanPham_tv_btn_moThuMucAnh);        //   EditText        ThemSuaXoaSanPham_edt_tenSanPham = findViewById(R.id.themSuaXoaSanPham_edt_tenSanPham);        ThemSuaXoaSanPham_edt_giaSanPham = findViewById(R.id.themSuaXoaSanPham_edt_giaSanPham);        ThemSuaXoaSanPham_edt_xuatXuSanPham = findViewById(R.id.themSuaXoaSanPham_edt_xuatXuSanPham);        ThemSuaXoaSanPham_edt_loaiSanPham = findViewById(R.id.themSuaXoaSanPham_edt_loaiSanPham);        ThemSuaXoaSanPham_edt_ngayNhapSanPham = findViewById(R.id.themSuaXoaSanPham_edt_ngayNhapSanPham);        //   LinearLayout        ThemSuaXoaSanPham_llout_btn_xoaSanPham = findViewById(R.id.themSuaXoaSanPham_llout_btn_xoaSanPham);        ThemSuaXoaSanPham_llout_btn_suaSanPham = findViewById(R.id.themSuaXoaSanPham_llout_btn_suaSanPham);        ThemSuaXoaSanPham_llout_btn_themSanPham = findViewById(R.id.themSuaXoaSanPham_llout_btn_themSanPham);    }}