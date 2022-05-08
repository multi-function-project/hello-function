// jQuery
$(function () {
  $("#button").click(function (event) {
    const data = {
      message: "ABC",
    };

    $.ajax({
      type: "POST",
      headers: {
        "x-api-key": "30文字のキー",
        // "Content-Type": "application/json",
      },
      data: JSON.stringify(data), // JSONデータ本体
      url: "http://hogehoge",
      dataType: "json",
    })
      // Ajaxリクエストが成功した場合
      .done(function (data) {
        $("#result").html(data);
        // success
        //取得jsonデータ
        var data_stringify = JSON.stringify(data);
        var data_json = JSON.parse(data_stringify);
        //jsonデータから各データを取得
        //出力
        $("#span1").text(data_json.result.message);
      })
      // Ajaxリクエストが失敗した場合
      .fail(function (XMLHttpRequest, textStatus, errorThrown) {
        console.log(errorThrown);
      });
  });
});
