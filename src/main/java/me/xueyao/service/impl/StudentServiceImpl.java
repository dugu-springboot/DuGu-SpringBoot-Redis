package me.xueyao.service.impl;

import com.alibaba.fastjson.JSONObject;
import me.xueyao.base.R;
import me.xueyao.domain.Student;
import me.xueyao.service.StudentService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon.Xue
 * @date 2021-09-04 00:35:47
 **/
@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    private RedisTemplate redisTemplate;


    @Override
    public R insert(Student student) {
        student.setCreateTm(new Date());
        student.setCreator("admin");
        String obj = JSONObject.toJSONString(student);
        redisTemplate.opsForValue().set("student", obj);
        return R.ofSuccess("添加成功");
    }

    @Override
    public R<Student> getById(String id) {
        Object obj = redisTemplate.opsForValue().get(id);
        if (Objects.isNull(obj)) {
            return R.ofSuccess("查询成功");
        }
        Student student = JSONObject.parseObject(String.valueOf(obj), Student.class);
        return R.ofSuccess("查询成功", student);
    }

    @Override
    public R update(Student student) {
        student.setUpdater("admin");
        student.setUpdateTm(new Date());
        String str = JSONObject.toJSONString(student);
        redisTemplate.opsForValue().set("student", str);
        return R.ofSuccess("更新成功");
    }

    @Override
    public R delete(String id) {
        redisTemplate.expire(id, 0, TimeUnit.SECONDS);
        return R.ofSuccess("删除成功");
    }
}
