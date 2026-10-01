-- Ho ten: Nguyen Thien Nhan - MSSV: 24110291 - De 04
-- Chay tren SQL Server (SSMS)
IF DB_ID(N'LTWebDe04') IS NULL CREATE DATABASE LTWebDe04;
GO
USE LTWebDe04;
GO

IF OBJECT_ID('OrderDetails') IS NOT NULL DROP TABLE OrderDetails;
IF OBJECT_ID('Orders')       IS NOT NULL DROP TABLE Orders;
IF OBJECT_ID('Favorites')    IS NOT NULL DROP TABLE Favorites;
IF OBJECT_ID('Shares')       IS NOT NULL DROP TABLE Shares;
IF OBJECT_ID('Videos')       IS NOT NULL DROP TABLE Videos;
IF OBJECT_ID('Category')     IS NOT NULL DROP TABLE Category;
IF OBJECT_ID('Users')        IS NOT NULL DROP TABLE Users;
GO

CREATE TABLE Users (
    Username nvarchar(50)  NOT NULL PRIMARY KEY,
    Password nvarchar(50)  NULL,
    Phone    nvarchar(15)  NULL,
    Fullname nvarchar(50)  NULL,
    Email    nvarchar(150) NULL,
    Admin    bit NULL,
    Active   bit NULL,
    Images   nvarchar(500) NULL
);

CREATE TABLE Category (
    CategoryId   int IDENTITY(1,1) NOT NULL PRIMARY KEY,
    Categoryname nvarchar(100) NULL,
    Categorycode nvarchar(100) NULL,
    Images       nvarchar(500) NULL,
    Status       bit NULL
);

CREATE TABLE Videos (
    VideoId     nvarchar(50)  NOT NULL PRIMARY KEY,
    Title       nvarchar(200) NULL,
    Poster      nvarchar(50)  NULL,
    Views       int NULL,
    Description nvarchar(500) NULL,
    Active      bit NULL,
    CategoryId  int NULL REFERENCES Category(CategoryId)
);

CREATE TABLE Shares (
    ShareId    int IDENTITY(1,1) NOT NULL PRIMARY KEY,
    Emails     nvarchar(50) NULL,
    SharedDate date NULL,
    Username   nvarchar(50) NULL REFERENCES Users(Username),
    VideoId    nvarchar(50) NULL REFERENCES Videos(VideoId)
);

CREATE TABLE Favorites (
    FavoriteId int IDENTITY(1,1) NOT NULL PRIMARY KEY,
    LikedDate  date NULL,
    VideoId    nvarchar(50) NULL REFERENCES Videos(VideoId),
    Username   nvarchar(50) NULL REFERENCES Users(Username)
);
GO

-- Users (admin/123, user1..user8 / 123)
INSERT INTO Users VALUES (N'admin', N'123', N'0900000000', N'Quan Tri Vien', N'admin@example.com', 1, 1, NULL);
INSERT INTO Users VALUES (N'user1', N'123', N'0900000001', N'Nguyen Van A', N'user1@example.com', 0, 1, NULL);
INSERT INTO Users VALUES (N'user2', N'123', N'0900000002', N'Tran Thi B',   N'user2@example.com', 0, 1, NULL);
INSERT INTO Users VALUES (N'user3', N'123', N'0900000003', N'Le Van C',     N'user3@example.com', 0, 1, NULL);
INSERT INTO Users VALUES (N'user4', N'123', N'0900000004', N'Pham Thi D',   N'user4@example.com', 0, 1, NULL);
INSERT INTO Users VALUES (N'user5', N'123', N'0900000005', N'Hoang Van E',  N'user5@example.com', 0, 1, NULL);
INSERT INTO Users VALUES (N'user6', N'123', N'0900000006', N'Vo Thi F',     N'user6@example.com', 0, 0, NULL);
INSERT INTO Users VALUES (N'user7', N'123', N'0900000007', N'Dang Van G',   N'user7@example.com', 0, 1, NULL);
INSERT INTO Users VALUES (N'user8', N'123', N'0900000008', N'Bui Thi H',    N'user8@example.com', 0, 1, NULL);

-- Category
INSERT INTO Category (Categoryname, Categorycode, Images, Status) VALUES (N'Am nhac',   N'MUSIC',  NULL, 1);
INSERT INTO Category (Categoryname, Categorycode, Images, Status) VALUES (N'The thao',  N'SPORT',  NULL, 1);
INSERT INTO Category (Categoryname, Categorycode, Images, Status) VALUES (N'Giai tri',  N'ENTER',  NULL, 1);
INSERT INTO Category (Categoryname, Categorycode, Images, Status) VALUES (N'Giao duc',  N'EDU',    NULL, 1);

-- Videos (Poster = ten file anh trong webapp/images, hoac URL)
INSERT INTO Videos VALUES (N'V001', N'Video am nhac 1',  N'https://picsum.photos/seed/v001/300/180', 120, N'Mo ta video am nhac 1',  1, 1);
INSERT INTO Videos VALUES (N'V002', N'Video am nhac 2',  N'https://picsum.photos/seed/v002/300/180', 90,  N'Mo ta video am nhac 2',  1, 1);
INSERT INTO Videos VALUES (N'V003', N'Video am nhac 3',  N'https://picsum.photos/seed/v003/300/180', 75,  N'Mo ta video am nhac 3',  1, 1);
INSERT INTO Videos VALUES (N'V004', N'Video am nhac 4',  N'https://picsum.photos/seed/v004/300/180', 60,  N'Mo ta video am nhac 4',  1, 1);
INSERT INTO Videos VALUES (N'V005', N'Video am nhac 5',  N'https://picsum.photos/seed/v005/300/180', 45,  N'Mo ta video am nhac 5',  1, 1);
INSERT INTO Videos VALUES (N'V006', N'Video the thao 1', N'https://picsum.photos/seed/v006/300/180', 300, N'Mo ta video the thao 1', 1, 2);
INSERT INTO Videos VALUES (N'V007', N'Video the thao 2', N'https://picsum.photos/seed/v007/300/180', 210, N'Mo ta video the thao 2', 1, 2);
INSERT INTO Videos VALUES (N'V008', N'Video the thao 3', N'https://picsum.photos/seed/v008/300/180', 150, N'Mo ta video the thao 3', 1, 2);
INSERT INTO Videos VALUES (N'V009', N'Video giai tri 1', N'https://picsum.photos/seed/v009/300/180', 500, N'Mo ta video giai tri 1', 1, 3);
INSERT INTO Videos VALUES (N'V010', N'Video giai tri 2', N'https://picsum.photos/seed/v010/300/180', 420, N'Mo ta video giai tri 2', 1, 3);
INSERT INTO Videos VALUES (N'V011', N'Video giao duc 1', N'https://picsum.photos/seed/v011/300/180', 80,  N'Mo ta video giao duc 1', 1, 4);
INSERT INTO Videos VALUES (N'V012', N'Video giao duc 2', N'https://picsum.photos/seed/v012/300/180', 65,  N'Mo ta video giao duc 2', 1, 4);

-- Shares
INSERT INTO Shares (Emails, SharedDate, Username, VideoId) VALUES (N'a@gmail.com', '2026-09-01', N'user1', N'V001');
INSERT INTO Shares (Emails, SharedDate, Username, VideoId) VALUES (N'b@gmail.com', '2026-09-02', N'user2', N'V001');
INSERT INTO Shares (Emails, SharedDate, Username, VideoId) VALUES (N'c@gmail.com', '2026-09-03', N'user3', N'V006');
INSERT INTO Shares (Emails, SharedDate, Username, VideoId) VALUES (N'd@gmail.com', '2026-09-04', N'user1', N'V009');

-- Favorites
INSERT INTO Favorites (LikedDate, VideoId, Username) VALUES ('2026-09-01', N'V001', N'user1');
INSERT INTO Favorites (LikedDate, VideoId, Username) VALUES ('2026-09-02', N'V001', N'user2');
INSERT INTO Favorites (LikedDate, VideoId, Username) VALUES ('2026-09-02', N'V001', N'user3');
INSERT INTO Favorites (LikedDate, VideoId, Username) VALUES ('2026-09-03', N'V006', N'user1');
INSERT INTO Favorites (LikedDate, VideoId, Username) VALUES ('2026-09-04', N'V009', N'user2');
GO

-- Bang Orders (Don hang COD & 8 trang thai)
CREATE TABLE Orders (
    OrderId INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    Username NVARCHAR(50) NULL REFERENCES Users(Username),
    ReceiverName NVARCHAR(100) NOT NULL,
    ReceiverPhone NVARCHAR(20) NOT NULL,
    ReceiverAddress NVARCHAR(255) NOT NULL,
    Note NVARCHAR(500) NULL,
    TotalAmount DECIMAL(18,2) NOT NULL,
    PaymentMethod NVARCHAR(50) NOT NULL DEFAULT N'COD',
    Status NVARCHAR(50) NOT NULL DEFAULT N'Đơn hàng mới',
    CreatedAt DATETIME DEFAULT GETDATE()
);

-- Bang OrderDetails (Chi tiet san pham trong don hang)
CREATE TABLE OrderDetails (
    DetailId INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    OrderId INT NOT NULL REFERENCES Orders(OrderId) ON DELETE CASCADE,
    VideoId NVARCHAR(50) NOT NULL REFERENCES Videos(VideoId),
    Price DECIMAL(18,2) NOT NULL,
    Quantity INT NOT NULL,
    Amount DECIMAL(18,2) NOT NULL
);
GO
