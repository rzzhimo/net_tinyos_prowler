# coding: UTF-8
import re
import json

# 记得把data.txt路径换为自己的data.txt路径
with open('data.txt', 'r') as f:
    log_data_str = f.read()

# 将log_data_str转为JSON对象
log_data_js = json.loads(log_data_str)


messages_list = log_data_js["message"]

# 生命期走到终止的message数量
drop_count = 0

# 发送总的信息数量
total_count = 0
for obj in messages_list:
    # 第四次迭代增加的功能，如果msg_ttl大于等于0说明收到过消息，否则说明没收到消息
    if obj["msg_ttl"] >= 0:
        total_count += 1

print("总的信息数量为："+str(total_count))

drop_count = 0
for obj in messages_list:
    if obj["msg_ttl"] == 0 or obj["corrupted"]:
        drop_count += 1

drop_ratio = drop_count / total_count
print('所有损坏或生命期走到终止的message的比例为：{:.2%}'.format(drop_ratio))

ttl_zero = 0
for obj in messages_list:
    if obj["msg_ttl"] == 0:
        ttl_zero += 1

ttl_zero_ratio = ttl_zero / total_count
print('其中生命期走到终止的message的比例为：{:.2%}'.format(ttl_zero_ratio))

drop_sent = 0
for obj in messages_list:
    if obj["sent"] and obj["corrupted"]:
        drop_sent += 1
drop_sent_ratio = drop_sent / total_count
print('因为成功到达被drop的message的比例为：{:.2%}'.format(drop_sent_ratio))

drop_buff = 0
for obj in messages_list:
    if obj["corrupted"] and obj["msg_cache_size"]>=10:
        if obj["msg_cache_size"]>1:
            print(obj)
        drop_buff += 1
drop_buff_ratio = drop_buff / total_count
print('半途因为缓冲区容量满被drop的message的比例为：{:.2%}'.format(drop_buff_ratio))
# 这条应该一直为0，因为模拟引擎，只有一个点一开始发了一条信息，之后只是同一条消息在点之间传播，
# 而缓冲区容量我设为了10，所以不可能出现缓冲区容量满的情况。

corrupted_voice = 0
for obj in messages_list:
    if obj["corrupted"] and not obj["sent"] and not  obj["msg_ttl"] == 0:
        corrupted_voice += 1

corrupted_voice_ratio = corrupted_voice / total_count
print('因为传输时噪音过大损坏的message的比例为：{:.2%}'.format(corrupted_voice_ratio))