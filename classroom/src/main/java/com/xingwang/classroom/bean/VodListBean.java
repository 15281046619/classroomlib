package com.xingwang.classroom.bean;


import com.xinwang.bgqbaselib.http.CommonEntity;

import java.util.List;

/**
 * Date:2020/8/19
 * Time;17:29
 * author:baiguiqiang
 */
public class VodListBean extends CommonEntity {


    /**
     * data : {"RequestId":"75F3D0CF-417A-497A-89A6-63197A8B4C55","Total":1,"LiveRecordVideoList":{"LiveRecordVideo":[{"PlaylistId":"c0a2dd0186f943b4b6beda0e2a1d4ca4","StreamName":"223","RecordStartTime":"2020-08-13T11:25:29Z","DomainName":"zhibo.xw518.com","RecordEndTime":"2020-08-13T12:56:15Z","Video":{"Status":"Normal","ModifyTime":"2020-08-13 21:00:11","VideoId":"f486420e804e4430a456260dbf351d60","Description":"223|zhibo|zhibo.xw518.com","Size":1344299008,"CreateTime":"2020-08-13 20:59:18","DownloadSwitch":"on","Title":"223","Duration":5413.24,"CustomerId":1960209066008730,"CreationTime":"2020-08-13T12:59:18Z","CoverURL":"http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00005.jpg","Snapshots":{"Snapshot":["http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00001.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00002.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00003.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00004.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00005.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00006.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00007.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00008.jpg"]},"TemplateGroupId":"VOD_NO_TRANSCODE"},"AppName":"zhibo"}]}}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * RequestId : 75F3D0CF-417A-497A-89A6-63197A8B4C55
         * Total : 1
         * LiveRecordVideoList : {"LiveRecordVideo":[{"PlaylistId":"c0a2dd0186f943b4b6beda0e2a1d4ca4","StreamName":"223","RecordStartTime":"2020-08-13T11:25:29Z","DomainName":"zhibo.xw518.com","RecordEndTime":"2020-08-13T12:56:15Z","Video":{"Status":"Normal","ModifyTime":"2020-08-13 21:00:11","VideoId":"f486420e804e4430a456260dbf351d60","Description":"223|zhibo|zhibo.xw518.com","Size":1344299008,"CreateTime":"2020-08-13 20:59:18","DownloadSwitch":"on","Title":"223","Duration":5413.24,"CustomerId":1960209066008730,"CreationTime":"2020-08-13T12:59:18Z","CoverURL":"http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00005.jpg","Snapshots":{"Snapshot":["http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00001.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00002.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00003.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00004.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00005.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00006.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00007.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00008.jpg"]},"TemplateGroupId":"VOD_NO_TRANSCODE"},"AppName":"zhibo"}]}
         */

        private String RequestId;
        private int Total;
        private LiveRecordVideoListBean LiveRecordVideoList;

        public String getRequestId() {
            return RequestId;
        }

        public void setRequestId(String RequestId) {
            this.RequestId = RequestId;
        }

        public int getTotal() {
            return Total;
        }

        public void setTotal(int Total) {
            this.Total = Total;
        }

        public LiveRecordVideoListBean getLiveRecordVideoList() {
            return LiveRecordVideoList;
        }

        public void setLiveRecordVideoList(LiveRecordVideoListBean LiveRecordVideoList) {
            this.LiveRecordVideoList = LiveRecordVideoList;
        }

        public static class LiveRecordVideoListBean {
            private List<LiveRecordVideoBean> LiveRecordVideo;

            public List<LiveRecordVideoBean> getLiveRecordVideo() {
                return LiveRecordVideo;
            }

            public void setLiveRecordVideo(List<LiveRecordVideoBean> LiveRecordVideo) {
                this.LiveRecordVideo = LiveRecordVideo;
            }

            public static class LiveRecordVideoBean {
                /**
                 * PlaylistId : c0a2dd0186f943b4b6beda0e2a1d4ca4
                 * StreamName : 223
                 * RecordStartTime : 2020-08-13T11:25:29Z
                 * DomainName : zhibo.xw518.com
                 * RecordEndTime : 2020-08-13T12:56:15Z
                 * Video : {"Status":"Normal","ModifyTime":"2020-08-13 21:00:11","VideoId":"f486420e804e4430a456260dbf351d60","Description":"223|zhibo|zhibo.xw518.com","Size":1344299008,"CreateTime":"2020-08-13 20:59:18","DownloadSwitch":"on","Title":"223","Duration":5413.24,"CustomerId":1960209066008730,"CreationTime":"2020-08-13T12:59:18Z","CoverURL":"http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00005.jpg","Snapshots":{"Snapshot":["http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00001.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00002.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00003.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00004.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00005.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00006.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00007.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00008.jpg"]},"TemplateGroupId":"VOD_NO_TRANSCODE"}
                 * AppName : zhibo
                 */

                private String PlaylistId;
                private String StreamName;
                private String RecordStartTime;
                private String DomainName;
                private String RecordEndTime;
                private VideoBean Video;
                private String AppName;

                public String getPlaylistId() {
                    return PlaylistId;
                }

                public void setPlaylistId(String PlaylistId) {
                    this.PlaylistId = PlaylistId;
                }

                public String getStreamName() {
                    return StreamName;
                }

                public void setStreamName(String StreamName) {
                    this.StreamName = StreamName;
                }

                public String getRecordStartTime() {
                    return RecordStartTime;
                }

                public void setRecordStartTime(String RecordStartTime) {
                    this.RecordStartTime = RecordStartTime;
                }

                public String getDomainName() {
                    return DomainName;
                }

                public void setDomainName(String DomainName) {
                    this.DomainName = DomainName;
                }

                public String getRecordEndTime() {
                    return RecordEndTime;
                }

                public void setRecordEndTime(String RecordEndTime) {
                    this.RecordEndTime = RecordEndTime;
                }

                public VideoBean getVideo() {
                    return Video;
                }

                public void setVideo(VideoBean Video) {
                    this.Video = Video;
                }

                public String getAppName() {
                    return AppName;
                }

                public void setAppName(String AppName) {
                    this.AppName = AppName;
                }

                public static class VideoBean {
                    /**
                     * Status : Normal
                     * ModifyTime : 2020-08-13 21:00:11
                     * VideoId : f486420e804e4430a456260dbf351d60
                     * Description : 223|zhibo|zhibo.xw518.com
                     * Size : 1344299008
                     * CreateTime : 2020-08-13 20:59:18
                     * DownloadSwitch : on
                     * Title : 223
                     * Duration : 5413.24
                     * CustomerId : 1960209066008730
                     * CreationTime : 2020-08-13T12:59:18Z
                     * CoverURL : http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00005.jpg
                     * Snapshots : {"Snapshot":["http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00001.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00002.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00003.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00004.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00005.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00006.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00007.jpg","http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00008.jpg"]}
                     * TemplateGroupId : VOD_NO_TRANSCODE
                     */

                    private String Status;
                    private String ModifyTime;
                    private String VideoId;
                    private String Description;
                    private int Size;
                    private String CreateTime;
                    private String DownloadSwitch;
                    private String Title;
                    private double Duration;
                    private long CustomerId;
                    private String CreationTime;
                    private String CoverURL;
                    private SnapshotsBean Snapshots;
                    private String TemplateGroupId;

                    public String getStatus() {
                        return Status;
                    }

                    public void setStatus(String Status) {
                        this.Status = Status;
                    }

                    public String getModifyTime() {
                        return ModifyTime;
                    }

                    public void setModifyTime(String ModifyTime) {
                        this.ModifyTime = ModifyTime;
                    }

                    public String getVideoId() {
                        return VideoId;
                    }

                    public void setVideoId(String VideoId) {
                        this.VideoId = VideoId;
                    }

                    public String getDescription() {
                        return Description;
                    }

                    public void setDescription(String Description) {
                        this.Description = Description;
                    }

                    public int getSize() {
                        return Size;
                    }

                    public void setSize(int Size) {
                        this.Size = Size;
                    }

                    public String getCreateTime() {
                        return CreateTime;
                    }

                    public void setCreateTime(String CreateTime) {
                        this.CreateTime = CreateTime;
                    }

                    public String getDownloadSwitch() {
                        return DownloadSwitch;
                    }

                    public void setDownloadSwitch(String DownloadSwitch) {
                        this.DownloadSwitch = DownloadSwitch;
                    }

                    public String getTitle() {
                        return Title;
                    }

                    public void setTitle(String Title) {
                        this.Title = Title;
                    }

                    public double getDuration() {
                        return Duration;
                    }

                    public void setDuration(double Duration) {
                        this.Duration = Duration;
                    }

                    public long getCustomerId() {
                        return CustomerId;
                    }

                    public void setCustomerId(long CustomerId) {
                        this.CustomerId = CustomerId;
                    }

                    public String getCreationTime() {
                        return CreationTime;
                    }

                    public void setCreationTime(String CreationTime) {
                        this.CreationTime = CreationTime;
                    }

                    public String getCoverURL() {
                        return CoverURL;
                    }

                    public void setCoverURL(String CoverURL) {
                        this.CoverURL = CoverURL;
                    }

                    public SnapshotsBean getSnapshots() {
                        return Snapshots;
                    }

                    public void setSnapshots(SnapshotsBean Snapshots) {
                        this.Snapshots = Snapshots;
                    }

                    public String getTemplateGroupId() {
                        return TemplateGroupId;
                    }

                    public void setTemplateGroupId(String TemplateGroupId) {
                        this.TemplateGroupId = TemplateGroupId;
                    }

                    public static class SnapshotsBean {
                        private List<String> Snapshot;

                        public List<String> getSnapshot() {
                            return Snapshot;
                        }

                        public void setSnapshot(List<String> Snapshot) {
                            this.Snapshot = Snapshot;
                        }
                    }
                }
            }
        }
    }
}
