
layui.use(['jquery','table','form'], function(){
  var table = layui.table;
  var form = layui.form;
  var $ = layui.$;

  var tt = table.render({
    elem: '#questionList'
    ,url: '/essayQuestion/essayQuestionVoSearch' //数据接口
    ,page: true //开启分页
    ,cols: [[
      {field: 'title', title: '题目'}
      ,{field: 'score', title: '分数'}
      ,{field: 'count', title: '待审数量'}
      ,{fixed: 'right', align:'center', toolbar: '#questionListBar'}
    ]]
    ,id: 'questionList'
  });

  table.on('tool(questionListFilter)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
    var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）

    if(layEvent === 'detail'){
      WeAdminShow(" 阅卷",'/essayQuestion/essayReviewPage?id='+data.id);
    }
  });

  form.on('submit(sreach)', function(data){
    tt.reload({where:data.field,page:{page:1}});
    return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
  });
});


