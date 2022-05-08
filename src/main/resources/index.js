// jQuery
$(function () {
  $("#button").click(function (event) {
    const data = {
      message: "ABC",
    };

    $.ajax({
      type: "POST",
      data: JSON.stringify(data), // JSONデータ本体
      url: "https://gonqxn392c.execute-api.ap-northeast-1.amazonaws.com/1-0-0/hello",
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
        alert(errorThrown);
      });
  });
});
