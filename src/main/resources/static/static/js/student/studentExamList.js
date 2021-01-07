
layui.use(['jquery','table','form'], function(){
  var table = layui.table;
  var $ = layui.$;
  var form = layui.form;
  var courseMap;
  var professionMap;

  var getMap = function(){
    $.ajax({
      url:'/student/courseMap',
      type:'get',
      success:function(data){
        courseMap =data;
      },
      error:function(error){
        layer.msg('失败,请刷新');
      }
    });

    $.ajax({
      url:'/student/professionMap',
      type:'get',
      success:function(data){
        professionMap = data;
      },
      error:function(error){
        layer.msg('失败,请刷新');
      }
    });
  }

  getMap();

  var tt = table.render({
    elem: '#studentList'
    ,url: '/student/studentExamSearch' //数据接口
    ,page: true //开启分页
    ,limit: 50
    ,toolbar: true
    ,defaultToolbar: ['filter', 'exports', 'print']
    ,limits:[100, 200, 400, 800, 2000]
    ,cols: [[
      {field: 'name', title: '名字',templet: '<div><span>{{d.student.name}}</span></div>'}
      ,{field: 'nianJi', title: '年级',sort:true,templet: '<div><span>{{d.student.nianJi}}</span></div>'}
      ,{field: 'courseName', title: '课程',sort:true,templet: function(d){
        if (courseMap==null||courseMap==undefined){
          getMap();
        }
          if (courseMap==null||courseMap==undefined){
            alert("网络开小差，请稍等一下");
          }
          return courseMap[d.courseId];
        }}
      ,{field: 'score', title: '分数',sort:true}
      ,{field: 'professionName', title: '专业',templet: function(d){
          if (courseMap==null||courseMap==undefined){
            getMap();
          }
          return professionMap[d.student.professionId];
        }}
      ,{field: 'idCard', title: '身份证',templet: '<div><span>{{d.student.idCard}}</span></div>'}
       //,{fixed: 'right', align:'center', toolbar: '#studentListBar'}
    ]]
    ,id: 'studentList'
  });

  form.on('submit(sreach)', function(data){
    tt.reload({where:data.field,page:{page:1}});
    return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
  });

  //table.on('toolbar(studentListFilter)',function(obj){})

});


