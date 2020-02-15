
# coding: utf-8

# In[ ]:


import cv2
import numpy as np


# In[ ]:

#taken from discussion
def make_frame_smaller(frame,ratio):
    # get size from a matrix
    height = frame.shape[0]
    width = frame.shape[1]
    #resize using cv2.resize(...)
    result = cv2.resize(frame,(int(width*ratio),int(height*ratio)))
    return result


# In[ ]:


def background_removal(frame):
                
    framehsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
        
         
    # skin color tange in HSV
    lower_skin = np.array([0,10,60], dtype= "uint8")
    upper_skin = np.array([50,170,255], dtype = "uint8")
        
#    lower_skin = np.array([0,58,50], dtype= "uint8")
#    upper_skin = np.array([30,255,255], dtype = "uint8")
    #extract skin color images
    mask = cv2.inRange(framehsv, lower_skin, upper_skin)
    
    #cv2.imshow("maks1", mask)
    
    #remove mask from image an blur it
    res = cv2.bitwise_and(framehsv, framehsv, mask= mask)
    median = cv2.medianBlur(res, 15)
    #cv2.imshow("res1", res)
    #cv2.imshow("med", median)
    return median


# In[ ]:


def template_matching(frame,template,original):
    #font = cv2.FONT_HERSHEY_SIMPLEX
    #convert them to grayscale
    _, _, v1 = cv2.split(frame)
    #cv2.imshow("v11", v1)
    #convert to black and white image
    thresh = 40
    bwv1 = cv2.threshold(v1, thresh, 255, cv2.THRESH_BINARY)[1]
    cv2.imshow("bwv1", bwv1)
    
    #initialize variables
    w = [0, 0, 0, 0, 0, 0, 0]
    h = [0, 0, 0, 0, 0, 0, 0]
    result = [0, 0, 0, 0, 0, 0, 0]
    xloc = [0, 0, 0, 0, 0, 0, 0]
    yloc = [0, 0, 0, 0, 0, 0, 0]
    font = cv2.FONT_HERSHEY_SIMPLEX
    
    #gestures that can be recognized
    gesture = ["One", "Two", "Three", "Four", "Five", "Thumbs Up", "Wave"]
    for i in range(len(template)):
        #template match
        thresh = 30
        template[i] = cv2.threshold(template[i], thresh, 255, cv2.THRESH_BINARY)[1]
        #cv2.imshow("bwtemp", template[i])
        w[i], h[i] = template[i].shape[::-1]
        res = cv2.matchTemplate(bwv1, template[i], cv2.TM_CCOEFF_NORMED)
        #max loc gives location, max val gives value of template match
        min_val, max_val, min_loc, max_loc = cv2.minMaxLoc(res)
        xloc[i], yloc[i] = max_loc
        result[i] = max_val   
                
    
    #get the best match
    resBig = result.index(max(result))
    bottom_right = (xloc[resBig] + w[resBig], yloc[resBig] + h[resBig]) #you may use the width and height of the template in here instead.
    
    #draw a green bounding box on the original image for visualization. 
    
    #set a threshold
    if result[resBig] > 0.5:
        cv2.rectangle(original,(xloc[resBig],yloc[resBig]), bottom_right, (0,255,0), 2)
        cv2.putText(original, gesture[resBig],(0,50), font, 2, (0,0,255), 3, cv2.LINE_AA)
    else:
        cv2.putText(original, "Move hand for better capture angle",(0,50), font, 2, (0,0,255), 3, cv2.LINE_AA)
    return original,bwv1


# In[ ]:


#read threshold images and store them as black and white
cap = cv2.VideoCapture(0)
one = cv2.imread("OneB.png")
two = cv2.imread("TwoB.png")
three = cv2.imread("ThreeB.png")
four = cv2.imread("FourB.png")
five = cv2.imread("FiveB.png")
thumbsup = cv2.imread("ThumbsUpB.png")
wave = cv2.imread("Wave.png")

bwone = cv2.cvtColor(one,cv2.COLOR_BGR2GRAY)
bwtwo = cv2.cvtColor(two,cv2.COLOR_BGR2GRAY)
bwthree = cv2.cvtColor(three,cv2.COLOR_BGR2GRAY)
bwfour = cv2.cvtColor(four,cv2.COLOR_BGR2GRAY)
bwfive = cv2.cvtColor(five,cv2.COLOR_BGR2GRAY)
bwthumbsup = cv2.cvtColor(thumbsup,cv2.COLOR_BGR2GRAY)
wave = cv2.cvtColor(wave,cv2.COLOR_BGR2GRAY)

thresh = 30
bwone = cv2.threshold(bwone, thresh, 255, cv2.THRESH_BINARY)[1]
bwtwo = cv2.threshold(bwtwo, thresh, 255, cv2.THRESH_BINARY)[1]
bwthree = cv2.threshold(bwthree, thresh, 255, cv2.THRESH_BINARY)[1]
bwfour = cv2.threshold(bwfour, thresh, 255, cv2.THRESH_BINARY)[1]
bwfive = cv2.threshold(bwfive, thresh, 255, cv2.THRESH_BINARY)[1]
bwthumbsup = cv2.threshold(bwthumbsup, thresh, 255, cv2.THRESH_BINARY)[1]
wave = cv2.threshold(wave, thresh, 255, cv2.THRESH_BINARY)[1]

bwone = cv2.medianBlur(bwone, 15)
bwtwo = cv2.medianBlur(bwtwo, 15)
bwthree = cv2.medianBlur(bwthree, 15)
bwfour = cv2.medianBlur(bwfour, 15)
bwfive = cv2.medianBlur(bwfive, 15)
bwthumbsup = cv2.medianBlur(bwthumbsup, 15)
wave = cv2.medianBlur(wave, 15)

cv2.imshow("bwone", bwone)
cv2.imshow("bwtwo", bwtwo)
cv2.imshow("bwthree", bwthree)
cv2.imshow("bwfour", bwfour)
cv2.imshow("bwfive", bwfive)
cv2.imshow("bwthumbsup", bwthumbsup)
cv2.imshow("wave", wave)

template = [bwone, bwtwo, bwthree, bwfour, bwfive, bwthumbsup, wave]


#template = cv2.imread("One.png")
while(True):
    ret, frame = cap.read()
    
    #make your frames smaller
    frame = make_frame_smaller(frame,0.9)
    original = frame.copy()
    
    #remove your back ground
    frame = background_removal(frame)
    
    #template matching for your object
    original, frame = template_matching(frame,template,original)
    
    cv2.imshow('frame',original)
    
    #press q to stop capturing frames
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cv2.destroyAllWindows()
cap.release()