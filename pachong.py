#!/usr/local/bin/python3.6
import urllib.parse,pymysql
import hashlib
import urllib.request
import random,re
import http.cookiejar
import sys

import base64



my_headers = [
"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 OPR/26.0.1656.60",
"Opera/8.0 (Windows NT 5.1; U; en)",
"Mozilla/5.0 (Windows NT 5.1; U; en; rv:1.8.1) Gecko/20061208 Firefox/2.0.0 Opera 9.50",
"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; en) Opera 9.50",
"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0",
"Mozilla/5.0 (X11; U; Linux x86_64; zh-CN; rv:1.9.2.10) Gecko/20100922 Ubuntu/10.10 (maverick) Firefox/3.6.10",
"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36",
"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11",
"Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.16 (KHTML, like Gecko) Chrome/10.0.648.133 Safari/534.16",
]   

def open_url_with_post(url,data):
    req = urllib.request.Request(url,data)
    req.add_header('User-Agent', random.choice(my_headers))
    cjar = http.cookiejar.CookieJar()
    opener = urllib.request.build_opener(urllib.request.HTTPCookieProcessor(cjar))
    urllib.request.install_opener(opener) 
    urllib.request.urlopen(req)


def open_url_with_get(url):
    req = urllib.request.Request(url)
    req.add_header('User-Agent', random.choice(my_headers))
    request = urllib.request.urlopen(req).read().decode('gbk')
    return request

# 转换数据指纹(md5加密)
def md5_data(data):
    hl = hashlib.md5()
    str_ = data
    hl.update(str_.encode(encoding='utf-8'))
    return hl.hexdigest()

# 删除末尾中文
def del_zh(word):
    zh_pattern = re.compile('[\u4e00-\u9fa5]+')
    match = zh_pattern.findall(word)
    result = word.replace(match[0],'')
    return result

# 登录函数
def login(user, password):
    url = 'http://210.47.163.27:9002/loginAction.do'
    data = urllib.parse.urlencode({
        'zjh':user,
        'mm':password,
        }).encode('utf-8')
    open_url_with_post(url,data)

# 生成爬取学生课程表正则表达式
def zz_sc_student(n):
    zz = 'this.className=\'evenfocus\';">'
    # 主条的选项 item
    item = '.*?<td rowspan="%d" >.*?&nbsp;(.*?)</td>' % n
    
    # 分条的选项
    jie = '.*?<td>&nbsp;(.*?)</td>' * 7
    zz = zz + item * 10 + jie *n
    return zz

# 生成爬取老师课程表正则表达式
def zz_sc_teacher(n):
    zz = r'''this.className='evenfocus';">\r\n\r\n<td rowspan=%s >&nbsp;(.*?)</td>\r\n\r\n<td rowspan=\d >&nbsp;\r\n<a href="(.*?)" target="_blank" style="TEXT-DECORATION:underline">\r\n(.*?)\r\n</a>''' % n
    item = '''.*?&nbsp;(.*?)</td>''' * 3 
    jie = '''.*?&nbsp;(.*?)</td>''' *6*n
    zz = zz + item +jie
    return zz

# 保存个人信息(老师和学生)
def save_person_mysql(account, password,role,deleteFlag, username, sex, college, major, clazz, nianji, nation, politics, kaoqu, address, diplomas):
    conn = pymysql.connect(host = '210.47.163.68', port = 3306 ,user = 'root',passwd = 'snrj2016',db = 'ydsn',charset = 'utf8' )
    cur = conn.cursor()
    cur.execute("select * from user_info where account='%s'" % account)
    have = cur.fetchall()
    if have == ():
        cur.execute('''insert into user_info(account, password, role, deleteFlag, username, sex, college, major, clazz, nianji, nation, politics, kaoqu, address, diplomas) 
            values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')''' % (account, password,role,deleteFlag, username, sex, college, major, clazz, nianji, nation, politics, kaoqu, address, diplomas))
        conn.commit()
    cur.close()
    conn.close()

# 保存课程表数据库(学生)
def save_student_course_mysql(account,courseId,courseCode,courseName,courseNumber,credit,peiYangFangXiang,courseAttribute,testType,teacherName,studyType,selectState,week,xingQi,jieCi,jieShu,district,building,classroom):
    conn = pymysql.connect(host = '210.47.163.68', port = 3306 ,user = 'root',passwd = 'snrj2016',db = 'ydsn',charset = 'utf8' )
    cur = conn.cursor()
    cur.execute("select * from student_course where courseId='%s'" % (courseId))
    have = cur.fetchall()
    if have == ():
    # recordTime curdate()
        cur.execute('''insert into student_course(courseId,courseCode,courseName,courseNumber,credit,peiYangFangXiang,courseAttribute,testType,teacherName,studyType,selectState,week,xingQi,jieCi,jieShu,district,building,classroom) 
        values('%s','%s','%s','%s','%s', '%s','%s','%s','%s', '%s','%s', '%s','%s', '%s', '%s', '%s', '%s', '%s')''' % 
        (courseId,courseCode,courseName,courseNumber,credit,peiYangFangXiang,courseAttribute,testType,teacherName,studyType,selectState,week,xingQi,jieCi,jieShu,district,building,classroom))
        conn.commit()
      
    cur.execute("select * from student_course_list where courseId='%s' and account='%s'" % (courseId,account))
    have_ = cur.fetchall()
    if have_ == ():
        cur.execute('''insert into student_course_list(account, courseId) 
            values('%s','%s')''' % (account, courseId))
        conn.commit()

    cur.close()
    conn.close()

# 保存学生考表
def save_student_test(account,testId,testName,district,building,classroom,courseName,week,day,time,seat):
    conn = pymysql.connect(host = '210.47.163.68', port = 3306 ,user = 'root',passwd = 'snrj2016',db = 'ydsn',charset = 'utf8' )
    cur = conn.cursor()
    cur.execute("select * from student_test where testId='%s'" % testId)
    have = cur.fetchall()
    if have == ():
    # recordTime curdate()

        cur.execute('''insert into student_test(testId,testName,district,building,classroom,courseName,week,day,time,seat) 
            values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')''' % (testId,testName,district,building,classroom,courseName,week,day,time,seat))
        conn.commit()
     
    
    cur.execute("select * from student_test_list where testId='%s' and account='%s'" % (testId,account))
    have_ = cur.fetchall()
    if have_ == ():
        cur.execute('''insert into student_test_list(account, testId) 
            values('%s','%s')''' % (account, testId))
        conn.commit()
    cur.close()
    conn.close()

# 保存教师课程表
def save_teacher_course(id,account,courseId,courseName,courseNumber,restNumber,\
currentNumber,week,xingQi,jieCi,district,building,classroom):
    conn = pymysql.connect(host = '210.47.163.68', port = 3306 ,user = 'root',passwd = 'snrj2016',db = 'ydsn',charset = 'utf8' )
    cur = conn.cursor()
    cur.execute("select * from teacher_course where id='%s'" % id)
    have = cur.fetchall()
    if have == ():
        cur.execute('''insert into teacher_course (id,account,courseId,courseName,courseNumber,restNumber,\
currentNumber,week,xingQi,jieCi,district,building,classroom) \
values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')''' % (id,account,courseId,courseName,courseNumber,restNumber,\
currentNumber,week,xingQi,jieCi,district,building,classroom))
        conn.commit()
 
    cur.close()
    conn.close()

# 保存学生老师表和学生老师对应关系表
def save_teacher_student_and_teacher_student_list(courseId,studentId,name,gender,trainPlan,academy,major,majorDirection,studyWay,isUser):
    conn = pymysql.connect(host = '210.47.163.68', port = 3306 ,user = 'root',passwd = 'snrj2016',db = 'ydsn',charset = 'utf8' )
    cur = conn.cursor()
    cur.execute("select * from teacher_student where studentId='%s'" % studentId)
    have = cur.fetchall()
    if have == ():
        cur.execute('''insert into teacher_student (studentId,name,gender,trainPlan,academy,major,majorDirection,studyWay,isUser)
values('%s','%s','%s','%s','%s','%s','%s','%s','%d')''' % (studentId,name,gender,trainPlan,academy,major,majorDirection,studyWay,isUser))
        conn.commit()


    # 由于一个学生可能选过老师的同样的课,所以做两个字段的检测
    cur.execute("select * from teacher_student_list where studentId='%s' and courseId='%s'" % (studentId,courseId))
    have = cur.fetchall()
    if have == ():
        cur.execute('''insert into teacher_student_list (studentId,courseId)
values('%s','%s')''' % (studentId,courseId))
        conn.commit()

    cur.close()
    conn.close()

# 保存学生成绩表
def save_student_score(gradeId,account,courseId,courseNumber,courseName,credit,type,grade,testTime,notPassedCause,gradeType,description):
    conn = pymysql.connect(host = '210.47.163.68', port = 3306 ,user = 'root',passwd = 'snrj2016',db = 'ydsn',charset = 'utf8' )
    cur = conn.cursor()
    cur.execute("select * from student_grade where gradeId='%s'" % gradeId)
    have = cur.fetchall()
    if have == ():
        cur.execute('''insert into student_grade(gradeId,account,courseId,courseNumber,courseName,credit,type,grade,testTime,notPassedCause,gradeType,description) 
                values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%d','%s')''' % (gradeId,account,courseId,courseNumber,courseName,credit,type,grade,testTime,notPassedCause,gradeType,description))
        conn.commit()

    cur.close()
    conn.close()

# 转换周次数据库
def zhuanhuan(week_often):
    num = []
    list_ke = []
    if '-' in week_often and ',' in week_often:
        week_often1 = week_often.split(',')
        for a in week_often1:
            a = a.strip()
            if '-' in a:
                a = a.split('-')
                list_ke =list_ke + [i for i in range(int(a[0]),int(a[1])+1)]
            else:
                num.append(a)
    elif '-' in week_often and ',' not in week_often:
        a = week_often.split('-')
        list_ke = list_ke + [i for i in range(int(a[0]),int(a[1])+1)]
    elif '-' not in week_often and ',' in week_often:
        week_often1 = week_often.split(',')
        num = [int(i) for i in week_often1]
    else:
        num.append(week_often)
    num = [int(i) for i in num]
    result = ''
    for item in list_ke + num:
        result = result + '|' + str(item)
    return result + '|'

# 抓取学生课程表
def course_list(user):
    url = 'http://210.47.163.27:9002/xkAction.do?actionType=6'
    data = open_url_with_get(url)
    # 一周有一节课的
    # 一周有两节课的
    # jieshu = 4
    id = 1
    # (courseId,courseCode,courseName,courseNumber,credit,peiYangFangXiang,
    # courseAttribute,testType,teacherName,studyType,selectState,week,xingQi,
    # jieCi,jieShu,district,building,classroom,recordTime)
    for jieshu in range(1,4):     
        zz = zz_sc_student(jieshu) 
        zz = re.compile(zz, re.S)
        result = zz.findall(data)
        for item in result:
            peiYangFangXiang = item[0]
            courseCode = item[1]
            courseName = item[2]
            courseNumber = item[3]
            credit = item[4]
            courseAttribute = item[5]
            testType = item[6]
            teacherName = item[7]
            studyType = item[8]
            selectState = item[9]
            for i in range(jieshu):
                week = item[10 + i*7]
                xingQi = item[11 + i*7]
                jieCi = item[12 + i*7]
                jieShu = item[13 + i*7]
                district = item[14 + i*7]
                building = item[15 + i*7]
                classroom = item[16 + i*7]
                week = zhuanhuan(del_zh(week))
                # str_ = courseCode + courseName + courseNumber + credit + peiYangFangXiang + courseAttribute + testType + teacherName + studyType + selectState + week + xingQi + jieCi + jieShu + district + building + classroom
                # hl.update(str_.encode(encoding='utf-8'))
                # courseId = hl.hexdigest()
                courseId = md5_data(courseCode + courseName + courseNumber + credit + peiYangFangXiang + courseAttribute + testType + teacherName + studyType + selectState + week + xingQi + jieCi + jieShu + district + building + classroom)
                save_student_course_mysql(user,courseId,courseCode,courseName,courseNumber,credit,peiYangFangXiang,courseAttribute,testType,teacherName,studyType,selectState,week,xingQi,jieCi,jieShu,district,building,classroom)
                id += 1

# 抓取个人信息爬取(教师和学生)
def person(account, password, isteacher):
    # 登录接口是一样的
    login(account, password)

    # 判断是否是老师
    if isteacher:
        url = 'http://210.47.163.27:9002/teacherGrxxAction.do?oper=jsxx'
        zz = r'<td align="left" width="275">&nbsp;\r\n\t\t\t\t\t\t\t\t\t(.*?)\r\n\t\t\t\t\t\t\t\t\t\r\n\t\t\t\t\t\t\t\t\t\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\t\t\t\t\t\t\t\t\t  \r\n\t\t\t\t\t\t\t\t\t</td>'
        zz = re.compile(zz, re.S)
        respond = open_url_with_get(url)
        result = zz.findall(respond)
        role = '2'
        username = result[0]
        sex = result[1]
        college = ''        #学院
        major = ''          #专业
        clazz = ''          #班级
        nianji = ''         #年级 
        nation = ''         #民族
        politics = ''       #政治面貌
        kaoqu = ''          #考区
        address = ''        #通讯地址
        diplomas = ''       #培养层次

    else:
        url = 'http://210.47.163.27:9002/xjInfoAction.do?oper=xjxx'
        # 由于名字和其他字段格式不一样所以匹配两次(学生)
        zz = r'''<td width="275">\r\n\t\t\t\t\t\t\t\t\t\t(.*?)\r\n\t\t\t\t\t\t\t\t\t\t</td>'''
        zz = re.compile(zz, re.S)
        respond = open_url_with_get(url)
        result = zz.findall(respond)
        username = result[1]
        zz = r'''<td align="left" width="275">\r\n\t\t\t\t\t\t\t\t\t\r\n\t\t\t\t\t\t\t\t\t  (.*?)\r\n\t\t\t\t\t\t\t\t\t</td>'''
        zz = re.compile(zz, re.S)
        result = zz.findall(respond)
        role = '1'
        sex = result[4]
        college = result[23]
        major = result[24]
        clazz = result[27]
        nianji = result[26]
        nation = result[9]
        politics = result[12]
        kaoqu = result[13]
        address = result[19]
        diplomas = result[35]
        

    # 用户角色？ 删除标记？ role deleteFlag
    password = base64.encodestring(password.encode('utf-8')).decode('utf8')
    # s2 = base64.decodestring(s1)
    deleteFlag = '1'
    save_person_mysql(account, password,role,deleteFlag, username, sex, college, major, clazz, nianji, nation, politics, kaoqu, address, diplomas)
    



# 爬取考试信息表
def kao_information(account):
    url = 'http://210.47.163.27:9002/ksApCxAction.do?oper=getKsapXx'
    respond = open_url_with_get(url)
    zz = r'''<tr class="odd">\r\n<td>(.*?)</td>\r\n<td>(.*?)</td>\r\n<td>(.*?)</td>\r\n<td>(.*?)</td>\r\n<td>(.*?)&nbsp;</td>\r\n<td>(.*?)</td>\t\r\n<td>(.*?)</td>\r\n<td>(.*?)</td>\r\n<td>(.*?)</td>\r\n<td>&nbsp;</td>\r\n'''
    zz = re.compile(zz, re.S)
    result = zz.findall(respond)
    for i in result:
        testName = i[0]
        district = i[1]
        building = i[2]
        classroom = i[3]
        courseName = i[4]
        week = i[5]
        day = i[6]
        time = i[7]
        seat = i[8]
        testId = md5_data(i[0]+i[1]+i[2]+i[3]+i[4]+i[5]+i[6]+i[7]+i[8])
        save_student_test(account,testId,testName,district,building,classroom,courseName,week,day,time,seat)

# 抓取学生成绩表
def student_score(account):
    # 及格成绩爬取
    # url = 'http://210.47.163.27:9002/gradeLnAllAction.do?type=ln&oper=qb'
    url = 'http://210.47.163.27:9002/gradeLnAllAction.do?type=ln&oper=qbinfo&lnxndm=2017-2018%D1%A7%C4%EA%C7%EF(%C1%BD%D1%A7%C6%DA)'
    respond = open_url_with_get(url)

    zz = r'''<td valign="middle">&nbsp;<b>(.*?)</b>\r\n\t\t\t\t\t&nbsp;</td>(.*?)<table width="100%" align="center" cellpadding="0" cellspacing="0">'''
    zz = re.compile(zz, re.S)
    result = zz.findall(respond)
    for i in result:
        description = i[0]
        zz2 = r'''<tr class="odd" onMouseOut="this.className=\'even\';" onMouseOver="this.className=\'evenfocus\';">\r\n\t\t\t<td align="center">\r\n                (.*?)\r\n            </td>\r\n            <td align="center">\r\n            \t (.*?)\r\n            </td>\r\n            <td align="center">\r\n                 (.*?)\r\n            </td>\r\n            <td align="center">\r\n                 (.*?)\r\n            </td>\r\n            <td align="center">\r\n                 (.*?)\r\n            </td>\r\n            <td align="center">\r\n                 (.*?)\r\n            </td>\r\n            <td align="center">\r\n            \r\n              \t{0,2}<p align="center">(.*?)&nbsp;</P>\r\n'''
        zz2 = re.compile(zz2, re.S)
        result2 = zz2.findall(i[1])
        for j in result2:      
            courseId = j[0]
            
            courseNumber = j[1]
            courseName = j[2]
            credit = j[4]
            type = j[5]
            grade = j[6]
            testTime = ''
            notPassedCause = ''
            gradeType = 1
            gradeId = md5_data(account+courseId+courseNumber+courseName+credit+type+grade+testTime+notPassedCause+str(gradeType)+description)
            save_student_score(gradeId,account,courseId,courseNumber,courseName,credit,type,grade,testTime,notPassedCause,gradeType,description)

    # 不及格成绩爬取
    url2 = 'http://210.47.163.27:9002/gradeLnAllAction.do?type=ln&oper=bjg'
    respond = open_url_with_get(url2)
    zz = r'''<td valign="middle">&nbsp;<b>\r\n                            \r\n                            (.*?)\r\n                            \r\n                            </b>(.*?)<table width="100%" align="center" cellpadding="0" cellspacing="0">'''
    zz = re.compile(zz, re.S)
    result = zz.findall(respond)
    for i in result:
        description = i[0]
        zz2 = r'''<tr class="odd" onMouseOut="this.className='even';" onMouseOver="this.className='evenfocus';">\r\n\t\t\t<td align="center">\r\n                (.*?)\r\n            </td>\r\n            <td align="center">\r\n            \t (.*?)\r\n            </td>\r\n            <td align="center">\r\n                 (.*?)\r\n            </td>\r\n            <td align="center">\r\n                 (.*?)            </td>\r\n            <td align="center">\r\n                 (.*?)\r\n            </td>\r\n            <td align="center">\r\n                 (.*?)\r\n            </td>\r\n           \r\n            <td align="center">\r\n              \t\t<p align="left">(.*?)&nbsp;</P>\r\n          \t</td>\r\n\r\n            \r\n          <td align="center">\r\n                 (.*?)\r\n            </td>\r\n            <!-- 081227hnn -->\r\n             <td align="center">\r\n                (.*?)&nbsp;'''
        zz2 = re.compile(zz2, re.S)
        result2 = zz2.findall(i[1])
        for j in result2:
            courseId = j[0]
            courseNumber = j[1]
            courseName = j[2]
            credit = j[4]
            type = j[5]
            grade = j[6]
            testTime = j[7]
            notPassedCause = j[8]
            gradeType = 2
            gradeId = md5_data(account+courseId+courseNumber+courseName+credit+type+grade+testTime+notPassedCause+str(gradeType)+description)
            save_student_score(gradeId,account,courseId,courseNumber,courseName,credit,type,grade,testTime,notPassedCause,gradeType,description)

# 抓取老师学生表
def teacher_student(url,courseId):
    url = 'http://210.47.163.27:9002/'+url
    response = open_url_with_get(url)
    zz = '''className='evenfocus';">.*?&nbsp;(.*?)</td>\r\n\r\n<td>&nbsp;(.*?)</td>\r\n\r\n<td>&nbsp;(.*?)</td>\r\n\r\n<td>&nbsp;(.*?)</td>\r\n\r\n<td>&nbsp;(.*?)</td>\r\n\r\n<td>&nbsp;(.*?)</td>\r\n\r\n<td>&nbsp;(.*?)</td>\r\n\r\n<td>&nbsp;(.*?)</td>\r\n\r\n</tr>'''
    zz = re.compile(zz, re.S)
    result = zz.findall(response)
    for i in result:
        # isUser 设置为1了 
        account = i[0]
        name = i[1]
        gender = i[2]
        trainPlan = i[3]
        academy = i[4]
        major = i[5]
        majorDirection = i[6]
        studyWay = i[7]
        isUser = 1
        studentId = md5_data(account+name+gender+trainPlan+academy+major+majorDirection+studyWay+str(isUser))
        save_teacher_student_and_teacher_student_list(courseId,studentId,name,gender,trainPlan,academy,major,majorDirection,studyWay,isUser)

# 抓取教师课程信息表
def teacher_course(account):
    url = 'http://210.47.163.27:9002/teacherXkAction.do?temp=1'
    respond = open_url_with_get(url)
    for jie in range(1,3):
        zz = zz_sc_teacher(jie)
        zz = re.compile(zz, re.S)
        result = zz.findall(respond)
        for i in result:
            courseId = i[0]
            courseName = i[2]
            courseNumber = i[3]
            restNumber = i[4]
            currentNumber = i[5]
            # 抓取教师学生表
            for a in range(1,jie+1):
                week = zhuanhuan(i[a*6][:-2])
                xingQi = i[a*6 + 1]
                jieCi = i[a*6 + 2]
                district = i[a*6 +3]
                building  = i[a*6 +4]
                classroom = i[a*6 + 5]
                id = md5_data(account+courseId+courseName+courseNumber+restNumber+\
currentNumber+week+xingQi+jieCi+district+building+classroom)
                save_teacher_course(id,account,courseId,courseName,courseNumber,restNumber,\
currentNumber,week,xingQi,jieCi,district,building,classroom)
                teacher_student(i[1],courseId)
    
# 清除这个账号所有相关的信息
def del_person_mysql(account):
    conn = pymysql.connect(host = '210.47.163.68', port = 3306 ,user = 'root',passwd = 'snrj2016',db = 'ydsn',charset = 'utf8' )
    cur = conn.cursor()
    # 删除学生个人信息
    cur.execute("delete from student_grade where account='%s';" % account)
    cur.execute("delete from student_course_list where account='%s';" % account)
    cur.execute("delete from student_test_list where account='%s';" % account)
    # 删除老师个人信息
    cur.execute("select * from teacher_course where account='%s'" % account)
    # print(cur.fetchall())
    for i in cur.fetchall():
        cur.execute("delete from teacher_student_list where courseId='%s'" % i[2])
    cur.execute("delete from teacher_course where account='%s'" % account)
    conn.commit()
    cur.close()
    conn.close()

# 对账号是学生的处理
def student(account,password):
    del_person_mysql(account)
    person(account,password,False)
    course_list(account)
    kao_information(account)
    student_score(account)

# 对账号是老师的处理
def teacher(account,password):
    del_person_mysql(account)
    person(account,password,True)
    teacher_course(account)

# 数据库中是否有学生的信息
def is_save(account):
    conn = pymysql.connect(host = '210.47.163.68', port = 3306 ,user = 'root',passwd = 'snrj2016',db = 'ydsn',charset = 'utf8' )
    cur = conn.cursor()
    cur.execute("select * from user_info where account='%s'" % account)
    have = cur.fetchall()

    cur.close()
    conn.close()
    password = base64.b64decode(have[0][1]).decode('utf8')
    return password

# 清空数据表(调试使用)
def restart():
    conn = pymysql.connect(host = '210.47.163.68', port = 3306 ,user = 'root',passwd = 'snrj2016',db = 'ydsn',charset = 'utf8' )
    cur = conn.cursor()
    # 清除所有学生信息
    cur.execute("TRUNCATE table student_course_list;")
    cur.execute("TRUNCATE table student_course")
    cur.execute("TRUNCATE table student_test_list")
    cur.execute("TRUNCATE table student_test")
    cur.execute("TRUNCATE table student_grade")

    # 清除教师信息
    cur.execute("TRUNCATE table teacher_student_list;")
    cur.execute("TRUNCATE table teacher_student;")
    cur.execute("TRUNCATE table teacher_course;")
    cur.execute("TRUNCATE table user_info;")
    print('清除所有数据成功')
    conn.commit()
    cur.close()
    conn.close()

# 调用主入口    
if __name__ == '__main__':
    account = '2016191007'
    password = 'ssgggqq!@#'
    # account = '2016101448'
    # password = 'b210464s'
    # account = '195140040'
    # password = "1937915896"
    # account = '1999500023'
    # password = 'abc12345'
    account = '195140050'
    password = '1'
    is_clear = False

    try:
        if not is_clear:
            # 判断是否是第一次爬取
            if len(sys.argv) == 2:
                # 重新爬取
                account = sys.argv[1]
                password = is_save(account)
            elif len(sys.argv) == 3:
                # 第一次爬取
                account = sys.argv[1]
                password = sys.argv[2]
            else:
                pass

            if account[4] == '5':
                teacher(account,password)
            else:
                student(account,password)
            # 成功返回1
            print(1)
        else:
            restart()
    except Exception as e:
        print(e)
        # 失败返回0
        print(0)
