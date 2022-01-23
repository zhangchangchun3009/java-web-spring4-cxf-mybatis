package pers.zcc.scm.web.pay.wechatpay.vo;

public class Resource {
    // 对开启结果数据进行加密的加密算法，目前只支持AEAD_AES_256_GCM
    private String algorithm;
    // Base64编码后的开启/停用结果数据密文
    private String ciphertext;
    // 附加数据
    private String associated_data;
    // 原始回调类型，为transaction
    private String original_type;
    // 加密使用的随机串
    private String nonce;

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getCiphertext() {
        return ciphertext;
    }

    public void setCiphertext(String ciphertext) {
        this.ciphertext = ciphertext;
    }

    public String getAssociated_data() {
        return associated_data;
    }

    public void setAssociated_data(String associated_data) {
        this.associated_data = associated_data;
    }

    public String getOriginal_type() {
        return original_type;
    }

    public void setOriginal_type(String original_type) {
        this.original_type = original_type;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    @Override
    public String toString() {
        return "Resource [algorithm=" + algorithm + ", ciphertext=" + ciphertext + ", associated_data="
                + associated_data + ", original_type=" + original_type + ", nonce=" + nonce + "]";
    }

}
