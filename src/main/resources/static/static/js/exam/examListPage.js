
layui.use(['jquery','table','form'], function(){
  var table = layui.table;
  var $ = layui.$;
  var now = new Date();
  var tt = table.render({
    elem: '#examList'
    ,url: '/exam/findCourseVoByStudent' //数据接口
    //,page: true //开启分页
    ,cols: [[
      {field: 'courseName', title: '所属课程'}
      ,{field: 'examName', title: '考试名称',templet:'<div>{{d.exam.examName}} </div>'}
      ,{field: 'score', title: '分数',templet:'<div>{{d.studentCourse.score}}</div>'}
      ,{field: 'startTime', title: '开始时间',templet: function(d){
            return '<span style="color: #822e2e;">'+ d.exam.startTime +'</span>'
        }
      },{field: 'endTime', title: '截止时间',templet: function(d){
          return '<span style="color: #ec5858;">'+ d.exam.endTime +'</span>'
        }
      },{field: 'example', title: '考试状态',templet: function(d){
          if (d.studentCourse.score>=60){
            return '<span style="color: #3974dc;"><i class="layui-icon">&#xe605;</i>已通过</span>'
          }else {
              if (now>d.exam.endTime){
                return '<span style="color: #cc0000;">未通过</span>'
              }
              if (now<d.exam.startTime){
                return '<span style="color: #1a1616;">未到考试时间</span>'
              }
              if (d.studentCourse.count>=d.exam.studentCount){
                  return '<span style="color: #1a1616;">已经参加了考试</span>'
              }
          }
          return '<a style="color: #ee1111;" href="examingPage?pageId='+d.exam.pageId +'&examId='+ d.exam.id +'&scId='+ d.studentCourse.id +'"><i class="layui-icon">&#xe6b2;</i> 进入考试</a>'
        }
      }
      ,{fixed: 'right', align:'center', toolbar: '#examListBar'}
    ]]
  });

  table.on('tool(examListFilter)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
    var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）

    if(layEvent === 'detail'){ //查看
      WeAdminShow(" 详细信息",'/exam/examDetails?id='+data.exam.id);
    }
  });


});


