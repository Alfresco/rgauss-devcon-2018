/*
 * Copyright 2018 Alfresco Software, Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.alfresco.example.rgauss.devcon2018;

/**
 * Representation of a content event from the Event Gateway
 */
public class ContentEvent
{
    private String contentId;
    private String contentUri;
    
    public String getContentId()
    {
        return contentId;
    }
    public void setContentId(String contentId)
    {
        this.contentId = contentId;
    }
    public String getContentUri()
    {
        return contentUri;
    }
    public void setContentUri(String contentUri)
    {
        this.contentUri = contentUri;
    }
}
