package com.xingwang.classroom.bean;

import com.xingwang.classroom.http.CommonEntity;

import java.util.List;

/**
 * Date:2020/8/20
 * Time;9:51
 * author:baiguiqiang
 */
public class PlayInfoBean extends CommonEntity {

    /**
     * data : {"VideoBase":{"Status":"Normal","VideoId":"f486420e804e4430a456260dbf351d60","TranscodeMode":"NoTranscode","CreationTime":"2020-08-13T12:59:18Z","Title":"223","MediaType":"video","CoverURL":"http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00005.jpg","Duration":"5413.24","OutputType":"cdn"},"RequestId":"62DEC19B-73AC-4872-B63B-378F65384871","PlayInfoList":{"PlayInfo":[{"Status":"Normal","StreamType":"video","Size":1344298985,"Definition":"OD","Fps":"25.0","Duration":"5413.24","ModificationTime":"2020-08-13T12:59:18Z","Specification":"Original","Bitrate":"1986.683","Encrypt":0,"PreprocessStatus":"UnPreprocess","Format":"m3u8","PlayURL":"http://app-vod.xw518.com/liveRecord/VOD_NO_TRANSCODE/zhibo/223/2020-08-13-19-25-30_2020-08-13-20-56-16.m3u8","NarrowBandType":"0","CreationTime":"2020-08-13T12:59:18Z","Height":720,"Width":1280,"JobId":"f486420e804e4430a456260dbf351d6002"}]}}
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
         * VideoBase : {"Status":"Normal","VideoId":"f486420e804e4430a456260dbf351d60","TranscodeMode":"NoTranscode","CreationTime":"2020-08-13T12:59:18Z","Title":"223","MediaType":"video","CoverURL":"http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00005.jpg","Duration":"5413.24","OutputType":"cdn"}
         * RequestId : 62DEC19B-73AC-4872-B63B-378F65384871
         * PlayInfoList : {"PlayInfo":[{"Status":"Normal","StreamType":"video","Size":1344298985,"Definition":"OD","Fps":"25.0","Duration":"5413.24","ModificationTime":"2020-08-13T12:59:18Z","Specification":"Original","Bitrate":"1986.683","Encrypt":0,"PreprocessStatus":"UnPreprocess","Format":"m3u8","PlayURL":"http://app-vod.xw518.com/liveRecord/VOD_NO_TRANSCODE/zhibo/223/2020-08-13-19-25-30_2020-08-13-20-56-16.m3u8","NarrowBandType":"0","CreationTime":"2020-08-13T12:59:18Z","Height":720,"Width":1280,"JobId":"f486420e804e4430a456260dbf351d6002"}]}
         */

        private VideoBaseBean VideoBase;
        private String RequestId;
        private PlayInfoListBean PlayInfoList;

        public VideoBaseBean getVideoBase() {
            return VideoBase;
        }

        public void setVideoBase(VideoBaseBean VideoBase) {
            this.VideoBase = VideoBase;
        }

        public String getRequestId() {
            return RequestId;
        }

        public void setRequestId(String RequestId) {
            this.RequestId = RequestId;
        }

        public PlayInfoListBean getPlayInfoList() {
            return PlayInfoList;
        }

        public void setPlayInfoList(PlayInfoListBean PlayInfoList) {
            this.PlayInfoList = PlayInfoList;
        }

        public static class VideoBaseBean {
            /**
             * Status : Normal
             * VideoId : f486420e804e4430a456260dbf351d60
             * TranscodeMode : NoTranscode
             * CreationTime : 2020-08-13T12:59:18Z
             * Title : 223
             * MediaType : video
             * CoverURL : http://app-vod.xw518.com/f486420e804e4430a456260dbf351d60/snapshots/99e3bf07a0dc483abe31cb69a6d2f6ab-00005.jpg
             * Duration : 5413.24
             * OutputType : cdn
             */

            private String Status;
            private String VideoId;
            private String TranscodeMode;
            private String CreationTime;
            private String Title;
            private String MediaType;
            private String CoverURL;
            private String Duration;
            private String OutputType;

            public String getStatus() {
                return Status;
            }

            public void setStatus(String Status) {
                this.Status = Status;
            }

            public String getVideoId() {
                return VideoId;
            }

            public void setVideoId(String VideoId) {
                this.VideoId = VideoId;
            }

            public String getTranscodeMode() {
                return TranscodeMode;
            }

            public void setTranscodeMode(String TranscodeMode) {
                this.TranscodeMode = TranscodeMode;
            }

            public String getCreationTime() {
                return CreationTime;
            }

            public void setCreationTime(String CreationTime) {
                this.CreationTime = CreationTime;
            }

            public String getTitle() {
                return Title;
            }

            public void setTitle(String Title) {
                this.Title = Title;
            }

            public String getMediaType() {
                return MediaType;
            }

            public void setMediaType(String MediaType) {
                this.MediaType = MediaType;
            }

            public String getCoverURL() {
                return CoverURL;
            }

            public void setCoverURL(String CoverURL) {
                this.CoverURL = CoverURL;
            }

            public String getDuration() {
                return Duration;
            }

            public void setDuration(String Duration) {
                this.Duration = Duration;
            }

            public String getOutputType() {
                return OutputType;
            }

            public void setOutputType(String OutputType) {
                this.OutputType = OutputType;
            }
        }

        public static class PlayInfoListBean {
            private List<PlayMessageBean> PlayInfo;

            public List<PlayMessageBean> getPlayInfo() {
                return PlayInfo;
            }

            public void setPlayInfo(List<PlayMessageBean> PlayInfo) {
                this.PlayInfo = PlayInfo;
            }

            public static class PlayMessageBean {
                /**
                 * Status : Normal
                 * StreamType : video
                 * Size : 1344298985
                 * Definition : OD
                 * Fps : 25.0
                 * Duration : 5413.24
                 * ModificationTime : 2020-08-13T12:59:18Z
                 * Specification : Original
                 * Bitrate : 1986.683
                 * Encrypt : 0
                 * PreprocessStatus : UnPreprocess
                 * Format : m3u8
                 * PlayURL : http://app-vod.xw518.com/liveRecord/VOD_NO_TRANSCODE/zhibo/223/2020-08-13-19-25-30_2020-08-13-20-56-16.m3u8
                 * NarrowBandType : 0
                 * CreationTime : 2020-08-13T12:59:18Z
                 * Height : 720
                 * Width : 1280
                 * JobId : f486420e804e4430a456260dbf351d6002
                 */

                private String Status;
                private String StreamType;
                private int Size;
                private String Definition;
                private String Fps;
                private String Duration;
                private String ModificationTime;
                private String Specification;
                private String Bitrate;
                private int Encrypt;
                private String PreprocessStatus;
                private String Format;
                private String PlayURL;
                private String NarrowBandType;
                private String CreationTime;
                private int Height;
                private int Width;
                private String JobId;

                public String getStatus() {
                    return Status;
                }

                public void setStatus(String Status) {
                    this.Status = Status;
                }

                public String getStreamType() {
                    return StreamType;
                }

                public void setStreamType(String StreamType) {
                    this.StreamType = StreamType;
                }

                public int getSize() {
                    return Size;
                }

                public void setSize(int Size) {
                    this.Size = Size;
                }

                public String getDefinition() {
                    return Definition;
                }

                public void setDefinition(String Definition) {
                    this.Definition = Definition;
                }

                public String getFps() {
                    return Fps;
                }

                public void setFps(String Fps) {
                    this.Fps = Fps;
                }

                public String getDuration() {
                    return Duration;
                }

                public void setDuration(String Duration) {
                    this.Duration = Duration;
                }

                public String getModificationTime() {
                    return ModificationTime;
                }

                public void setModificationTime(String ModificationTime) {
                    this.ModificationTime = ModificationTime;
                }

                public String getSpecification() {
                    return Specification;
                }

                public void setSpecification(String Specification) {
                    this.Specification = Specification;
                }

                public String getBitrate() {
                    return Bitrate;
                }

                public void setBitrate(String Bitrate) {
                    this.Bitrate = Bitrate;
                }

                public int getEncrypt() {
                    return Encrypt;
                }

                public void setEncrypt(int Encrypt) {
                    this.Encrypt = Encrypt;
                }

                public String getPreprocessStatus() {
                    return PreprocessStatus;
                }

                public void setPreprocessStatus(String PreprocessStatus) {
                    this.PreprocessStatus = PreprocessStatus;
                }

                public String getFormat() {
                    return Format;
                }

                public void setFormat(String Format) {
                    this.Format = Format;
                }

                public String getPlayURL() {
                    return PlayURL;
                }

                public void setPlayURL(String PlayURL) {
                    this.PlayURL = PlayURL;
                }

                public String getNarrowBandType() {
                    return NarrowBandType;
                }

                public void setNarrowBandType(String NarrowBandType) {
                    this.NarrowBandType = NarrowBandType;
                }

                public String getCreationTime() {
                    return CreationTime;
                }

                public void setCreationTime(String CreationTime) {
                    this.CreationTime = CreationTime;
                }

                public int getHeight() {
                    return Height;
                }

                public void setHeight(int Height) {
                    this.Height = Height;
                }

                public int getWidth() {
                    return Width;
                }

                public void setWidth(int Width) {
                    this.Width = Width;
                }

                public String getJobId() {
                    return JobId;
                }

                public void setJobId(String JobId) {
                    this.JobId = JobId;
                }
            }
        }
    }
}
