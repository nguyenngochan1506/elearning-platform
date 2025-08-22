// src/components/SocialSidebar.tsx

import { Avatar } from "@heroui/avatar";
import { Link } from "@heroui/link";
import { Button } from "@heroui/button";

// --- DỮ LIỆU GIẢ (MOCK DATA) ---

// 1. Thông báo
const notifications = [
  {
    id: 1,
    avatarUrl: "https://i.pravatar.cc/150?u=a042581f4e29026704d",
    text: "Phạm Nhật V. đã bình luận về bài giảng của bạn.",
    time: "5 phút trước",
  },
  {
    id: 2,
    avatarUrl: "https://i.pravatar.cc/150?u=a04258114e29026702d",
    text: "Khóa học ReactJS Toàn diện vừa có bài giảng mới.",
    time: "1 giờ trước",
  },
];

// 2. Tin nhắn gần đây
const recentMessages = [
  {
    id: 1,
    name: "Đặng Lê N.",
    avatarUrl: "https://i.pravatar.cc/150?u=a04258114e29026702d",
    lastMessage: "Cảm ơn bạn đã chia sẻ tài liệu nhé!",
    isOnline: true,
  },
  {
    id: 2,
    name: "Trần Anh H.",
    avatarUrl: "https://i.pravatar.cc/150?u=a042581f4e29026706d",
    lastMessage: "Bạn có thể xem lại đoạn code đó giúp...",
    isOnline: false,
  },
];

// 3. Danh sách bạn bè đang hoạt động
const onlineFriends = [
  {
    id: 1,
    name: "Phạm Nhật V.",
    avatarUrl: "https://i.pravatar.cc/150?u=a042581f4e29026704d",
  },
  {
    id: 2,
    name: "Elon M.",
    avatarUrl: "https://i.pravatar.cc/150?u=a042581f4e29026707d",
  },
  {
    id: 3,
    name: "Jeff B.",
    avatarUrl: "https://i.pravatar.cc/150?u=a042581f4e29026708d",
  },
];

// --- COMPONENT GIAO DIỆN ---

export const SocialSidebar = () => {
  return (
    <div className="space-y-8">
      {/* Phần Thông báo */}
      <section>
        <h3 className="font-bold text-lg mb-4">Thông báo</h3>
        <div className="space-y-4">
          {notifications.map((item) => (
            <Link
              key={item.id}
              className="flex items-start gap-3 w-full p-2 rounded-lg hover:bg-default-100"
              href="#"
            >
              <Avatar size="md" src={item.avatarUrl} />
              <div className="flex flex-col">
                <p className="text-sm text-default-800">{item.text}</p>
                <span className="text-xs text-default-500">{item.time}</span>
              </div>
            </Link>
          ))}
        </div>
        <Button
          className="mt-2 w-full"
          color="primary"
          size="sm"
          variant="light"
        >
          Xem tất cả
        </Button>
      </section>

      {/* Phần Tin nhắn */}
      <section>
        <h3 className="font-bold text-lg mb-4">Tin nhắn</h3>
        <div className="space-y-3">
          {recentMessages.map((item) => (
            <Link
              key={item.id}
              className="flex items-center gap-3 w-full p-2 rounded-lg hover:bg-default-100"
              href="#"
            >
              <div className="relative">
                <Avatar size="lg" src={item.avatarUrl} />
                {item.isOnline && (
                  <span className="absolute bottom-0 right-0 block h-3 w-3 rounded-full bg-green-500 ring-2 ring-white" />
                )}
              </div>
              <div className="flex flex-col">
                <p className="text-sm font-semibold text-default-800">
                  {item.name}
                </p>
                <p className="text-xs text-default-600 truncate max-w-[150px]">
                  {item.lastMessage}
                </p>
              </div>
            </Link>
          ))}
        </div>
        <Button
          className="mt-2 w-full"
          color="primary"
          size="sm"
          variant="light"
        >
          Mở trang nhắn tin
        </Button>
      </section>

      {/* Phần Bạn bè */}
      <section>
        <h3 className="font-bold text-lg mb-4">Bạn bè đang hoạt động</h3>
        <div className="space-y-3">
          {onlineFriends.map((item) => (
            <Link
              key={item.id}
              className="flex items-center gap-3 w-full p-2 rounded-lg hover:bg-default-100"
              href="#"
            >
              <Avatar size="md" src={item.avatarUrl} />
              <p className="text-sm font-medium text-default-800">
                {item.name}
              </p>
            </Link>
          ))}
        </div>
      </section>
    </div>
  );
};
