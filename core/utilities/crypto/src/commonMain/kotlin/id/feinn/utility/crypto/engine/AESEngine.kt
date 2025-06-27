package id.feinn.utility.crypto.engine

import id.feinn.utility.crypto.*
import id.feinn.utility.crypto.params.KeyParameter
import id.feinn.utility.crypto.util.Pack
import kotlin.properties.Delegates

public class AESEngine : DefaultMultiBlockCipher() {

    public companion object {

        private const val BLOCK_SIZE = 16

        private val S: ByteArray = byteArrayOf(
            99.toByte(), 124.toByte(), 119.toByte(), 123.toByte(), 242.toByte(), 107.toByte(), 111.toByte(), 197.toByte(),
            48.toByte(), 1.toByte(), 103.toByte(), 43.toByte(), 254.toByte(), 215.toByte(), 171.toByte(), 118.toByte(),
            202.toByte(), 130.toByte(), 201.toByte(), 125.toByte(), 250.toByte(), 89.toByte(), 71.toByte(), 240.toByte(),
            173.toByte(), 212.toByte(), 162.toByte(), 175.toByte(), 156.toByte(), 164.toByte(), 114.toByte(), 192.toByte(),
            183.toByte(), 253.toByte(), 147.toByte(), 38.toByte(), 54.toByte(), 63.toByte(), 247.toByte(), 204.toByte(),
            52.toByte(), 165.toByte(), 229.toByte(), 241.toByte(), 113.toByte(), 216.toByte(), 49.toByte(), 21.toByte(),
            4.toByte(), 199.toByte(), 35.toByte(), 195.toByte(), 24.toByte(), 150.toByte(), 5.toByte(), 154.toByte(),
            7.toByte(), 18.toByte(), 128.toByte(), 226.toByte(), 235.toByte(), 39.toByte(), 178.toByte(), 117.toByte(),
            9.toByte(), 131.toByte(), 44.toByte(), 26.toByte(), 27.toByte(), 110.toByte(), 90.toByte(), 160.toByte(),
            82.toByte(), 59.toByte(), 214.toByte(), 179.toByte(), 41.toByte(), 227.toByte(), 47.toByte(), 132.toByte(),
            83.toByte(), 209.toByte(), 0.toByte(), 237.toByte(), 32.toByte(), 252.toByte(), 177.toByte(), 91.toByte(),
            106.toByte(), 203.toByte(), 190.toByte(), 57.toByte(), 74.toByte(), 76.toByte(), 88.toByte(), 207.toByte(),
            208.toByte(), 239.toByte(), 170.toByte(), 251.toByte(), 67.toByte(), 77.toByte(), 51.toByte(), 133.toByte(),
            69.toByte(), 249.toByte(), 2.toByte(), 127.toByte(), 80.toByte(), 60.toByte(), 159.toByte(), 168.toByte(),
            81.toByte(), 163.toByte(), 64.toByte(), 143.toByte(), 146.toByte(), 157.toByte(), 56.toByte(), 245.toByte(),
            188.toByte(), 182.toByte(), 218.toByte(), 33.toByte(), 16.toByte(), 255.toByte(), 243.toByte(), 210.toByte(),
            205.toByte(), 12.toByte(), 19.toByte(), 236.toByte(), 95.toByte(), 151.toByte(), 68.toByte(), 23.toByte(),
            196.toByte(), 167.toByte(), 126.toByte(), 61.toByte(), 100.toByte(), 93.toByte(), 25.toByte(), 115.toByte(),
            96.toByte(), 129.toByte(), 79.toByte(), 220.toByte(), 34.toByte(), 42.toByte(), 144.toByte(), 136.toByte(),
            70.toByte(), 238.toByte(), 184.toByte(), 20.toByte(), 222.toByte(), 94.toByte(), 11.toByte(), 219.toByte(),
            224.toByte(), 50.toByte(), 58.toByte(), 10.toByte(), 73.toByte(), 6.toByte(), 36.toByte(), 92.toByte(),
            194.toByte(), 211.toByte(), 172.toByte(), 98.toByte(), 145.toByte(), 149.toByte(), 228.toByte(), 121.toByte(),
            231.toByte(), 200.toByte(), 55.toByte(), 109.toByte(), 141.toByte(), 213.toByte(), 78.toByte(), 169.toByte(),
            108.toByte(), 86.toByte(), 244.toByte(), 234.toByte(), 101.toByte(), 122.toByte(), 174.toByte(), 8.toByte(),
            186.toByte(), 120.toByte(), 37.toByte(), 46.toByte(), 28.toByte(), 166.toByte(), 180.toByte(), 198.toByte(),
            232.toByte(), 221.toByte(), 116.toByte(), 31.toByte(), 75.toByte(), 189.toByte(), 139.toByte(), 138.toByte(),
            112.toByte(), 62.toByte(), 181.toByte(), 102.toByte(), 72.toByte(), 3.toByte(), 246.toByte(), 14.toByte(),
            97.toByte(), 53.toByte(), 87.toByte(), 185.toByte(), 134.toByte(), 193.toByte(), 29.toByte(), 158.toByte(),
            225.toByte(), 248.toByte(), 152.toByte(), 17.toByte(), 105.toByte(), 217.toByte(), 142.toByte(), 148.toByte(),
            155.toByte(), 30.toByte(), 135.toByte(), 233.toByte(), 206.toByte(), 85.toByte(), 40.toByte(), 223.toByte(),
            140.toByte(), 161.toByte(), 137.toByte(), 13.toByte(), 191.toByte(), 230.toByte(), 66.toByte(), 104.toByte(),
            65.toByte(), 153.toByte(), 45.toByte(), 15.toByte(), 176.toByte(), 84.toByte(), 187.toByte(), 22.toByte()
        )

        private val Si: ByteArray = byteArrayOf(
            82.toByte(),   9.toByte(), 106.toByte(), 213.toByte(),  48.toByte(),  54.toByte(), 165.toByte(),  56.toByte(),
            191.toByte(),  64.toByte(), 163.toByte(), 158.toByte(), 129.toByte(), 243.toByte(), 215.toByte(), 251.toByte(),
            124.toByte(), 227.toByte(),  57.toByte(), 130.toByte(), 155.toByte(),  47.toByte(), 255.toByte(), 135.toByte(),
            52.toByte(), 142.toByte(),  67.toByte(),  68.toByte(), 196.toByte(), 222.toByte(), 233.toByte(), 203.toByte(),
            84.toByte(), 123.toByte(), 148.toByte(),  50.toByte(), 166.toByte(), 194.toByte(),  35.toByte(),  61.toByte(),
            238.toByte(),  76.toByte(), 149.toByte(),  11.toByte(),  66.toByte(), 250.toByte(), 195.toByte(),  78.toByte(),
            8.toByte(),  46.toByte(), 161.toByte(), 102.toByte(),  40.toByte(), 217.toByte(),  36.toByte(), 178.toByte(),
            118.toByte(),  91.toByte(), 162.toByte(),  73.toByte(), 109.toByte(), 139.toByte(), 209.toByte(),  37.toByte(),
            114.toByte(), 248.toByte(), 246.toByte(), 100.toByte(), 134.toByte(), 104.toByte(), 152.toByte(),  22.toByte(),
            212.toByte(), 164.toByte(),  92.toByte(), 204.toByte(),  93.toByte(), 101.toByte(), 182.toByte(), 146.toByte(),
            108.toByte(), 112.toByte(),  72.toByte(),  80.toByte(), 253.toByte(), 237.toByte(), 185.toByte(), 218.toByte(),
            94.toByte(),  21.toByte(),  70.toByte(),  87.toByte(), 167.toByte(), 141.toByte(), 157.toByte(), 132.toByte(),
            144.toByte(), 216.toByte(), 171.toByte(),   0.toByte(), 140.toByte(), 188.toByte(), 211.toByte(),  10.toByte(),
            247.toByte(), 228.toByte(),  88.toByte(),   5.toByte(), 184.toByte(), 179.toByte(),  69.toByte(),   6.toByte(),
            208.toByte(),  44.toByte(),  30.toByte(), 143.toByte(), 202.toByte(),  63.toByte(),  15.toByte(),   2.toByte(),
            193.toByte(), 175.toByte(), 189.toByte(),   3.toByte(),   1.toByte(),  19.toByte(), 138.toByte(), 107.toByte(),
            58.toByte(), 145.toByte(),  17.toByte(),  65.toByte(),  79.toByte(), 103.toByte(), 220.toByte(), 234.toByte(),
            151.toByte(), 242.toByte(), 207.toByte(), 206.toByte(), 240.toByte(), 180.toByte(), 230.toByte(), 115.toByte(),
            150.toByte(), 172.toByte(), 116.toByte(),  34.toByte(), 231.toByte(), 173.toByte(),  53.toByte(), 133.toByte(),
            226.toByte(), 249.toByte(),  55.toByte(), 232.toByte(),  28.toByte(), 117.toByte(), 223.toByte(), 110.toByte(),
            71.toByte(), 241.toByte(),  26.toByte(), 113.toByte(),  29.toByte(),  41.toByte(), 197.toByte(), 137.toByte(),
            111.toByte(), 183.toByte(),  98.toByte(),  14.toByte(), 170.toByte(),  24.toByte(), 190.toByte(),  27.toByte(),
            252.toByte(),  86.toByte(),  62.toByte(),  75.toByte(), 198.toByte(), 210.toByte(), 121.toByte(),  32.toByte(),
            154.toByte(), 219.toByte(), 192.toByte(), 254.toByte(), 120.toByte(), 205.toByte(),  90.toByte(), 244.toByte(),
            31.toByte(), 221.toByte(), 168.toByte(),  51.toByte(), 136.toByte(),   7.toByte(), 199.toByte(),  49.toByte(),
            177.toByte(),  18.toByte(),  16.toByte(),  89.toByte(),  39.toByte(), 128.toByte(), 236.toByte(),  95.toByte(),
            96.toByte(),  81.toByte(), 127.toByte(), 169.toByte(),  25.toByte(), 181.toByte(),  74.toByte(),  13.toByte(),
            45.toByte(), 229.toByte(), 122.toByte(), 159.toByte(), 147.toByte(), 201.toByte(), 156.toByte(), 239.toByte(),
            160.toByte(), 224.toByte(),  59.toByte(),  77.toByte(), 174.toByte(),  42.toByte(), 245.toByte(), 176.toByte(),
            200.toByte(), 235.toByte(), 187.toByte(),  60.toByte(), 131.toByte(),  83.toByte(), 153.toByte(),  97.toByte(),
            23.toByte(),  43.toByte(),   4.toByte(), 126.toByte(), 186.toByte(), 119.toByte(), 214.toByte(),  38.toByte(),
            225.toByte(), 105.toByte(),  20.toByte(),  99.toByte(),  85.toByte(),  33.toByte(),  12.toByte(), 125.toByte(),
        )

        private val rcon: List<Int> = listOf(
            0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a,
            0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91
        )

        private val T0: List<Int> = listOf(
            0xa56363c6.toInt(), 0x847c7cf8.toInt(), 0x997777ee.toInt(), 0x8d7b7bf6.toInt(), 0x0df2f2ff.toInt(),
            0xbd6b6bd6.toInt(), 0xb16f6fde.toInt(), 0x54c5c591.toInt(), 0x50303060.toInt(), 0x03010102.toInt(),
            0xa96767ce.toInt(), 0x7d2b2b56.toInt(), 0x19fefee7.toInt(), 0x62d7d7b5.toInt(), 0xe6abab4d.toInt(),
            0x9a7676ec.toInt(), 0x45caca8f.toInt(), 0x9d82821f.toInt(), 0x40c9c989.toInt(), 0x877d7dfa.toInt(),
            0x15fafaef.toInt(), 0xeb5959b2.toInt(), 0xc947478e.toInt(), 0x0bf0f0fb.toInt(), 0xecadad41.toInt(),
            0x67d4d4b3.toInt(), 0xfda2a25f.toInt(), 0xeaafaf45.toInt(), 0xbf9c9c23.toInt(), 0xf7a4a453.toInt(),
            0x967272e4.toInt(), 0x5bc0c09b.toInt(), 0xc2b7b775.toInt(), 0x1cfdfde1.toInt(), 0xae93933d.toInt(),
            0x6a26264c.toInt(), 0x5a36366c.toInt(), 0x413f3f7e.toInt(), 0x02f7f7f5.toInt(), 0x4fcccc83.toInt(),
            0x5c343468.toInt(), 0xf4a5a551.toInt(), 0x34e5e5d1.toInt(), 0x08f1f1f9.toInt(), 0x937171e2.toInt(),
            0x73d8d8ab.toInt(), 0x53313162.toInt(), 0x3f15152a.toInt(), 0x0c040408.toInt(), 0x52c7c795.toInt(),
            0x65232346.toInt(), 0x5ec3c39d.toInt(), 0x28181830.toInt(), 0xa1969637.toInt(), 0x0f05050a.toInt(),
            0xb59a9a2f.toInt(), 0x0907070e.toInt(), 0x36121224.toInt(), 0x9b80801b.toInt(), 0x3de2e2df.toInt(),
            0x26ebebcd.toInt(), 0x6927274e.toInt(), 0xcdb2b27f.toInt(), 0x9f7575ea.toInt(), 0x1b090912.toInt(),
            0x9e83831d.toInt(), 0x742c2c58.toInt(), 0x2e1a1a34.toInt(), 0x2d1b1b36.toInt(), 0xb26e6edc.toInt(),
            0xee5a5ab4.toInt(), 0xfba0a05b.toInt(), 0xf65252a4.toInt(), 0x4d3b3b76.toInt(), 0x61d6d6b7.toInt(),
            0xceb3b37d.toInt(), 0x7b292952.toInt(), 0x3ee3e3dd.toInt(), 0x712f2f5e.toInt(), 0x97848413.toInt(),
            0xf55353a6.toInt(), 0x68d1d1b9.toInt(), 0x00000000.toInt(), 0x2cededc1.toInt(), 0x60202040.toInt(),
            0x1ffcfce3.toInt(), 0xc8b1b179.toInt(), 0xed5b5bb6.toInt(), 0xbe6a6ad4.toInt(), 0x46cbcb8d.toInt(),
            0xd9bebe67.toInt(), 0x4b393972.toInt(), 0xde4a4a94.toInt(), 0xd44c4c98.toInt(), 0xe85858b0.toInt(),
            0x4acfcf85.toInt(), 0x6bd0d0bb.toInt(), 0x2aefefc5.toInt(), 0xe5aaaa4f.toInt(), 0x16fbfbed.toInt(),
            0xc5434386.toInt(), 0xd74d4d9a.toInt(), 0x55333366.toInt(), 0x94858511.toInt(), 0xcf45458a.toInt(),
            0x10f9f9e9.toInt(), 0x06020204.toInt(), 0x817f7ffe.toInt(), 0xf05050a0.toInt(), 0x443c3c78.toInt(),
            0xba9f9f25.toInt(), 0xe3a8a84b.toInt(), 0xf35151a2.toInt(), 0xfea3a35d.toInt(), 0xc0404080.toInt(),
            0x8a8f8f05.toInt(), 0xad92923f.toInt(), 0xbc9d9d21.toInt(), 0x48383870.toInt(), 0x04f5f5f1.toInt(),
            0xdfbcbc63.toInt(), 0xc1b6b677.toInt(), 0x75dadaaf.toInt(), 0x63212142.toInt(), 0x30101020.toInt(),
            0x1affffe5.toInt(), 0x0ef3f3fd.toInt(), 0x6dd2d2bf.toInt(), 0x4ccdcd81.toInt(), 0x140c0c18.toInt(),
            0x35131326.toInt(), 0x2fececc3.toInt(), 0xe15f5fbe.toInt(), 0xa2979735.toInt(), 0xcc444488.toInt(),
            0x3917172e.toInt(), 0x57c4c493.toInt(), 0xf2a7a755.toInt(), 0x827e7efc.toInt(), 0x473d3d7a.toInt(),
            0xac6464c8.toInt(), 0xe75d5dba.toInt(), 0x2b191932.toInt(), 0x957373e6.toInt(), 0xa06060c0.toInt(),
            0x98818119.toInt(), 0xd14f4f9e.toInt(), 0x7fdcdca3.toInt(), 0x66222244.toInt(), 0x7e2a2a54.toInt(),
            0xab90903b.toInt(), 0x8388880b.toInt(), 0xca46468c.toInt(), 0x29eeeec7.toInt(), 0xd3b8b86b.toInt(),
            0x3c141428.toInt(), 0x79dedea7.toInt(), 0xe25e5ebc.toInt(), 0x1d0b0b16.toInt(), 0x76dbdbad.toInt(),
            0x3be0e0db.toInt(), 0x56323264.toInt(), 0x4e3a3a74.toInt(), 0x1e0a0a14.toInt(), 0xdb494992.toInt(),
            0x0a06060c.toInt(), 0x6c242448.toInt(), 0xe45c5cb8.toInt(), 0x5dc2c29f.toInt(), 0x6ed3d3bd.toInt(),
            0xefacac43.toInt(), 0xa66262c4.toInt(), 0xa8919139.toInt(), 0xa4959531.toInt(), 0x37e4e4d3.toInt(),
            0x8b7979f2.toInt(), 0x32e7e7d5.toInt(), 0x43c8c88b.toInt(), 0x5937376e.toInt(), 0xb76d6dda.toInt(),
            0x8c8d8d01.toInt(), 0x64d5d5b1.toInt(), 0xd24e4e9c.toInt(), 0xe0a9a949.toInt(), 0xb46c6cd8.toInt(),
            0xfa5656ac.toInt(), 0x07f4f4f3.toInt(), 0x25eaeacf.toInt(), 0xaf6565ca.toInt(), 0x8e7a7af4.toInt(),
            0xe9aeae47.toInt(), 0x18080810.toInt(), 0xd5baba6f.toInt(), 0x887878f0.toInt(), 0x6f25254a.toInt(),
            0x722e2e5c.toInt(), 0x241c1c38.toInt(), 0xf1a6a657.toInt(), 0xc7b4b473.toInt(), 0x51c6c697.toInt(),
            0x23e8e8cb.toInt(), 0x7cdddda1.toInt(), 0x9c7474e8.toInt(), 0x211f1f3e.toInt(), 0xdd4b4b96.toInt(),
            0xdcbdbd61.toInt(), 0x868b8b0d.toInt(), 0x858a8a0f.toInt(), 0x907070e0.toInt(), 0x423e3e7c.toInt(),
            0xc4b5b571.toInt(), 0xaa6666cc.toInt(), 0xd8484890.toInt(), 0x05030306.toInt(), 0x01f6f6f7.toInt(),
            0x120e0e1c.toInt(), 0xa36161c2.toInt(), 0x5f35356a.toInt(), 0xf95757ae.toInt(), 0xd0b9b969.toInt(),
            0x91868617.toInt(), 0x58c1c199.toInt(), 0x271d1d3a.toInt(), 0xb99e9e27.toInt(), 0x38e1e1d9.toInt(),
            0x13f8f8eb.toInt(), 0xb398982b.toInt(), 0x33111122.toInt(), 0xbb6969d2.toInt(), 0x70d9d9a9.toInt(),
            0x898e8e07.toInt(), 0xa7949433.toInt(), 0xb69b9b2d.toInt(), 0x221e1e3c.toInt(), 0x92878715.toInt(),
            0x20e9e9c9.toInt(), 0x49cece87.toInt(), 0xff5555aa.toInt(), 0x78282850.toInt(), 0x7adfdfa5.toInt(),
            0x8f8c8c03.toInt(), 0xf8a1a159.toInt(), 0x80898909.toInt(), 0x170d0d1a.toInt(), 0xdabfbf65.toInt(),
            0x31e6e6d7.toInt(), 0xc6424284.toInt(), 0xb86868d0.toInt(), 0xc3414182.toInt(), 0xb0999929.toInt(),
            0x772d2d5a.toInt(), 0x110f0f1e.toInt(), 0xcbb0b07b.toInt(), 0xfc5454a8.toInt(), 0xd6bbbb6d.toInt(),
            0x3a16162c.toInt()
        )

        private val Tinv0: List<Int> = listOf(
            0x50a7f451.toInt(), 0x5365417e.toInt(), 0xc3a4171a.toInt(), 0x965e273a.toInt(), 0xcb6bab3b.toInt(),
            0xf1459d1f.toInt(), 0xab58faac.toInt(), 0x9303e34b.toInt(), 0x55fa3020.toInt(), 0xf66d76ad.toInt(),
            0x9176cc88.toInt(), 0x254c02f5.toInt(), 0xfcd7e54f.toInt(), 0xd7cb2ac5.toInt(), 0x80443526.toInt(),
            0x8fa362b5.toInt(), 0x495ab1de.toInt(), 0x671bba25.toInt(), 0x980eea45.toInt(), 0xe1c0fe5d.toInt(),
            0x02752fc3.toInt(), 0x12f04c81.toInt(), 0xa397468d.toInt(), 0xc6f9d36b.toInt(), 0xe75f8f03.toInt(),
            0x959c9215.toInt(), 0xeb7a6dbf.toInt(), 0xda595295.toInt(), 0x2d83bed4.toInt(), 0xd3217458.toInt(),
            0x2969e049.toInt(), 0x44c8c98e.toInt(), 0x6a89c275.toInt(), 0x78798ef4.toInt(), 0x6b3e5899.toInt(),
            0xdd71b927.toInt(), 0xb64fe1be.toInt(), 0x17ad88f0.toInt(), 0x66ac20c9.toInt(), 0xb43ace7d.toInt(),
            0x184adf63.toInt(), 0x82311ae5.toInt(), 0x60335197.toInt(), 0x457f5362.toInt(), 0xe07764b1.toInt(),
            0x84ae6bbb.toInt(), 0x1ca081fe.toInt(), 0x942b08f9.toInt(), 0x58684870.toInt(), 0x19fd458f.toInt(),
            0x876cde94.toInt(), 0xb7f87b52.toInt(), 0x23d373ab.toInt(), 0xe2024b72.toInt(), 0x578f1fe3.toInt(),
            0x2aab5566.toInt(), 0x0728ebb2.toInt(), 0x03c2b52f.toInt(), 0x9a7bc586.toInt(), 0xa50837d3.toInt(),
            0xf2872830.toInt(), 0xb2a5bf23.toInt(), 0xba6a0302.toInt(), 0x5c8216ed.toInt(), 0x2b1ccf8a.toInt(),
            0x92b479a7.toInt(), 0xf0f207f3.toInt(), 0xa1e2694e.toInt(), 0xcdf4da65.toInt(), 0xd5be0506.toInt(),
            0x1f6234d1.toInt(), 0x8afea6c4.toInt(), 0x9d532e34.toInt(), 0xa055f3a2.toInt(), 0x32e18a05.toInt(),
            0x75ebf6a4.toInt(), 0x39ec830b.toInt(), 0xaaef6040.toInt(), 0x069f715e.toInt(), 0x51106ebd.toInt(),
            0xf98a213e.toInt(), 0x3d06dd96.toInt(), 0xae053edd.toInt(), 0x46bde64d.toInt(), 0xb58d5491.toInt(),
            0x055dc471.toInt(), 0x6fd40604.toInt(), 0xff155060.toInt(), 0x24fb9819.toInt(), 0x97e9bdd6.toInt(),
            0xcc434089.toInt(), 0x779ed967.toInt(), 0xbd42e8b0.toInt(), 0x888b8907.toInt(), 0x385b19e7.toInt(),
            0xdbeec879.toInt(), 0x470a7ca1.toInt(), 0xe90f427c.toInt(), 0xc91e84f8.toInt(), 0x00000000.toInt(),
            0x83868009.toInt(), 0x48ed2b32.toInt(), 0xac70111e.toInt(), 0x4e725a6c.toInt(), 0xfbff0efd.toInt(),
            0x5638850f.toInt(), 0x1ed5ae3d.toInt(), 0x27392d36.toInt(), 0x64d90f0a.toInt(), 0x21a65c68.toInt(),
            0xd1545b9b.toInt(), 0x3a2e3624.toInt(), 0xb1670a0c.toInt(), 0x0fe75793.toInt(), 0xd296eeb4.toInt(),
            0x9e919b1b.toInt(), 0x4fc5c080.toInt(), 0xa220dc61.toInt(), 0x694b775a.toInt(), 0x161a121c.toInt(),
            0x0aba93e2.toInt(), 0xe52aa0c0.toInt(), 0x43e0223c.toInt(), 0x1d171b12.toInt(), 0x0b0d090e.toInt(),
            0xadc78bf2.toInt(), 0xb9a8b62d.toInt(), 0xc8a91e14.toInt(), 0x8519f157.toInt(), 0x4c0775af.toInt(),
            0xbbdd99ee.toInt(), 0xfd607fa3.toInt(), 0x9f2601f7.toInt(), 0xbcf5725c.toInt(), 0xc53b6644.toInt(),
            0x347efb5b.toInt(), 0x7629438b.toInt(), 0xdcc623cb.toInt(), 0x68fcedb6.toInt(), 0x63f1e4b8.toInt(),
            0xcadc31d7.toInt(), 0x10856342.toInt(), 0x40229713.toInt(), 0x2011c684.toInt(), 0x7d244a85.toInt(),
            0xf83dbbd2.toInt(), 0x1132f9ae.toInt(), 0x6da129c7.toInt(), 0x4b2f9e1d.toInt(), 0xf330b2dc.toInt(),
            0xec52860d.toInt(), 0xd0e3c177.toInt(), 0x6c16b32b.toInt(), 0x99b970a9.toInt(), 0xfa489411.toInt(),
            0x2264e947.toInt(), 0xc48cfca8.toInt(), 0x1a3ff0a0.toInt(), 0xd82c7d56.toInt(), 0xef903322.toInt(),
            0xc74e4987.toInt(), 0xc1d138d9.toInt(), 0xfea2ca8c.toInt(), 0x360bd498.toInt(), 0xcf81f5a6.toInt(),
            0x28de7aa5.toInt(), 0x268eb7da.toInt(), 0xa4bfad3f.toInt(), 0xe49d3a2c.toInt(), 0x0d927850.toInt(),
            0x9bcc5f6a.toInt(), 0x62467e54.toInt(), 0xc2138df6.toInt(), 0xe8b8d890.toInt(), 0x5ef7392e.toInt(),
            0xf5afc382.toInt(), 0xbe805d9f.toInt(), 0x7c93d069.toInt(), 0xa92dd56f.toInt(), 0xb31225cf.toInt(),
            0x3b99acc8.toInt(), 0xa77d1810.toInt(), 0x6e639ce8.toInt(), 0x7bbb3bdb.toInt(), 0x097826cd.toInt(),
            0xf418596e.toInt(), 0x01b79aec.toInt(), 0xa89a4f83.toInt(), 0x656e95e6.toInt(), 0x7ee6ffaa.toInt(),
            0x08cfbc21.toInt(), 0xe6e815ef.toInt(), 0xd99be7ba.toInt(), 0xce366f4a.toInt(), 0xd4099fea.toInt(),
            0xd67cb029.toInt(), 0xafb2a431.toInt(), 0x31233f2a.toInt(), 0x3094a5c6.toInt(), 0xc066a235.toInt(),
            0x37bc4e74.toInt(), 0xa6ca82fc.toInt(), 0xb0d090e0.toInt(), 0x15d8a733.toInt(), 0x4a9804f1.toInt(),
            0xf7daec41.toInt(), 0x0e50cd7f.toInt(), 0x2ff69117.toInt(), 0x8dd64d76.toInt(), 0x4db0ef43.toInt(),
            0x544daacc.toInt(), 0xdf0496e4.toInt(), 0xe3b5d19e.toInt(), 0x1b886a4c.toInt(), 0xb81f2cc1.toInt(),
            0x7f516546.toInt(), 0x04ea5e9d.toInt(), 0x5d358c01.toInt(), 0x737487fa.toInt(), 0x2e410bfb.toInt(),
            0x5a1d67b3.toInt(), 0x52d2db92.toInt(), 0x335610e9.toInt(), 0x1347d66d.toInt(), 0x8c61d79a.toInt(),
            0x7a0ca137.toInt(), 0x8e14f859.toInt(), 0x893c13eb.toInt(), 0xee27a9ce.toInt(), 0x35c961b7.toInt(),
            0xede51ce1.toInt(), 0x3cb1477a.toInt(), 0x59dfd29c.toInt(), 0x3f73f255.toInt(), 0x79ce1418.toInt(),
            0xbf37c773.toInt(), 0xeacdf753.toInt(), 0x5baafd5f.toInt(), 0x146f3ddf.toInt(), 0x86db4478.toInt(),
            0x81f3afca.toInt(), 0x3ec468b9.toInt(), 0x2c342438.toInt(), 0x5f40a3c2.toInt(), 0x72c31d16.toInt(),
            0x0c25e2bc.toInt(), 0x8b493c28.toInt(), 0x41950dff.toInt(), 0x7101a839.toInt(), 0xdeb30c08.toInt(),
            0x9ce4b4d8.toInt(), 0x90c15664.toInt(), 0x6184cb7b.toInt(), 0x70b632d5.toInt(), 0x745c6c48.toInt(),
            0x4257b8d0.toInt()
        )

        private fun shift(r: Int, shift: Int) : Int = (r ushr shift) or (r shl -shift)

        private const val m1: Int = 0x80808080.toInt()
        private const val m2: Int = 0x7f7f7f7f.toInt()
        private const val m3: Int = 0x0000001b.toInt()
        private const val m4: Int = 0xC0C0C0C0.toInt()
        private const val m5: Int = 0x3f3f3f3f.toInt()

        private fun FFmulX(x: Int): Int = (((x and AESEngine.m2) shl 1) xor (((x and AESEngine.m1) ushr 7) * AESEngine.m3))

        private fun FFmulX2(x: Int): Int {
            val t0: Int = (x and AESEngine.m5) shl 2
            var t1: Int = (x and AESEngine.m4)
            t1 = t1 xor (t1 ushr 1)
            return t0 xor (t1 ushr 2) xor (t1 ushr 5)
        }

        private fun inv_mcol(x: Int): Int {
            var t0 = x
            var t1 = t0 xor shift(t0, 8)
            t0 = t0 xor FFmulX(t1)
            t1 = t1 xor AESEngine.FFmulX2(t0)
            t0 = t0 xor (t1 xor AESEngine.shift(t1, 16))
            return t0
        }

        private fun subWord(x: Int): Int = (AESEngine.S[x and 255].toInt() and 255 or ((AESEngine.S[(x shr 8) and 255].toInt() and 255) shl 8) or ((AESEngine.S[(x shr 16) and 255].toInt() and 255) shl 16) or (AESEngine.S[(x shr 24) and 255].toInt() shl 24))

        public fun newInstance(): MultiBlockCipher = AESEngine()

    }

    @Throws(IllegalArgumentException::class)
    private fun generateWorkingKey(key: ByteArray, forEncryption: Boolean): List<IntArray> {
        val keyLen = key.size
        if (keyLen < 16 || keyLen > 32 || (keyLen and 7) != 0) {
            throw IllegalArgumentException("Key length not 128/192/256 bits.")
        }

        val KC = keyLen ushr 2
        ROUNDS = KC + 6
        val W: Array<IntArray> = Array(ROUNDS + 1) { IntArray(4)}

        when(KC) {
            4 -> {
                var col0 = Pack.littleEndianToInt(key, 0);      W[0][0] = col0
                var col1 = Pack.littleEndianToInt(key, 4);      W[0][1] = col1
                var col2 = Pack.littleEndianToInt(key, 8);      W[0][2] = col2
                var col3 = Pack.littleEndianToInt(key, 12);     W[0][3] = col3

                for (i in 1 .. 10) {
                    val colx = subWord(shift(col3, 8)) xor rcon[i - 1]
                    col0 = col0 xor colx;           W[i][0] = col0
                    col1 = col1 xor col0;           W[i][1] = col1
                    col2 = col2 xor col1;           W[i][2] = col2
                    col3 = col3 xor col2;           W[i][3] = col3
                }
            }

            6 -> {
                var col0 = Pack.littleEndianToInt(key, 0);      W[0][0] = col0
                var col1 = Pack.littleEndianToInt(key, 4);      W[0][1] = col1
                var col2 = Pack.littleEndianToInt(key, 8);      W[0][2] = col2
                var col3 = Pack.littleEndianToInt(key, 12);     W[0][3] = col3

                var col4 = Pack.littleEndianToInt(key, 16)
                var col5 = Pack.littleEndianToInt(key, 20)

                var i = 1; var rcon = 1; var colx: Int
                while (true) {
                    W[i][0] = col4
                    W[i][1] = col5
                    colx = subWord(shift(col5, 8)) xor rcon; rcon = rcon shl 1
                    col0 = col0 xor colx;           W[i][2] = col0
                    col1 = col1 xor col0;           W[i][3] = col1

                    col2 = col2 xor col1;           W[i + 1][0] = col2
                    col3 = col3 xor col2;           W[i + 1][1] = col3
                    col4 = col4 xor col3;           W[i + 1][2] = col4
                    col5 = col5 xor col4;           W[i + 1][3] = col5

                    colx = subWord(shift(col5, 8)) xor rcon; rcon = rcon shl 1
                    col0 = col0 xor colx;           W[i + 2][0] = col0
                    col1 = col1 xor col0;           W[i + 2][1] = col1
                    col2 = col2 xor col1;           W[i + 2][2] = col2
                    col3 = col3 xor col2;           W[i + 2][3] = col3

                    i += 3
                    if (i >= 13) break

                    col4 = col4 xor col3
                    col5 = col5 xor col4
                }
            }

            8 -> {
                var col0 = Pack.littleEndianToInt(key, 0);      W[0][0] = col0
                var col1 = Pack.littleEndianToInt(key, 4);      W[0][1] = col1
                var col2 = Pack.littleEndianToInt(key, 8);      W[0][2] = col2
                var col3 = Pack.littleEndianToInt(key, 12);     W[0][3] = col3

                var col4 = Pack.littleEndianToInt(key, 16);      W[1][0] = col4
                var col5 = Pack.littleEndianToInt(key, 20);      W[1][1] = col5
                var col6 = Pack.littleEndianToInt(key, 24);      W[1][2] = col6
                var col7 = Pack.littleEndianToInt(key, 28);      W[1][3] = col7

                var i = 1; var rcon = 1; var colx: Int
                while (true) {
                    colx = subWord(shift(col7, 8)) xor rcon; rcon = rcon shl 1
                    col0 = col0 xor colx;           W[i][0] = col0
                    col1 = col1 xor col0;           W[i][1] = col1
                    col2 = col2 xor col1;           W[i][2] = col2
                    col3 = col3 xor col2;           W[i][3] = col3
                    ++i

                    if (i >= 15) break

                    colx = subWord(col3)
                    col4 = col4 xor colx;           W[i][0] = col4
                    col5 = col5 xor col4;           W[i][1] = col5
                    col6 = col6 xor col5;           W[i][2] = col6
                    col7 = col7 xor col6;           W[i][3] = col7
                    ++i
                }
            }

            else -> throw IllegalStateException("Should never get here")
        }

        if (!forEncryption) {
            for (j in 1 until ROUNDS) {
                for (i in 0 until 4) {
                    W[j][i] = inv_mcol(W[j][i])
                }
            }
        }

        return W.toList()
    }

    private var ROUNDS by Delegates.notNull<Int>()
    private var WorkingKey: List<IntArray>? = null
    private var forEncryption by Delegates.notNull<Boolean>()

    private var s by Delegates.notNull<ByteArray>()

    override fun init(
        forEncryption: Boolean,
        params: CipherParameters
    ) {
        if (params is KeyParameter) {
            WorkingKey = generateWorkingKey(params.getKey(), forEncryption)
            this.forEncryption = forEncryption
            if (forEncryption) {
                s = S.copyOf()
            } else {
                s = Si.copyOf()
            }

            return
        }

        throw IllegalArgumentException("invalid parameter passed to AES init - " + params::class.simpleName)
    }

    override fun getAlgorithmName(): String = "AES"

    override fun getBlockSize(): Int = BLOCK_SIZE

    override fun processBlock(input: ByteArray, inOff: Int, output: ByteArray, outOff: Int): Int {
        if (WorkingKey == null) throw IllegalStateException("AES engine not initialised")
        if (inOff > (input.size - BLOCK_SIZE)) throw DataLengthException("input buffer too short")
        if (outOff > (output.size - BLOCK_SIZE)) throw OutputLengthException("output buffer too short")
        if (forEncryption) encryptBlock(input, inOff, output, outOff, WorkingKey!!)
        else decryptBlock(input, inOff, output, outOff, WorkingKey!!)

        return BLOCK_SIZE
    }

    override fun reset() {}

    private fun encryptBlock(`in`: ByteArray, inOff: Int, out: ByteArray, outOff: Int, KW: List<IntArray>) {
        var C0 = Pack.littleEndianToInt(`in`, inOff + 0)
        var C1 = Pack.littleEndianToInt(`in`, inOff + 4)
        var C2 = Pack.littleEndianToInt(`in`, inOff + 8)
        var C3 = Pack.littleEndianToInt(`in`, inOff + 12)

        var t0: Int = C0 xor KW[0][0]
        var t1: Int = C1 xor KW[0][1]
        var t2: Int = C2 xor KW[0][2]

        var r: Int = 1; var r0: Int; var r1: Int; var r2: Int; var r3: Int = C3 xor KW[0][3]
        while (r < ROUNDS - 1) {
            r0 = T0[t0 and 0xff] xor shift(T0[(t1 shr 8) and 0xff], 24) xor shift(T0[(t2 shr 16) and 0xff], 16) xor shift(T0[(r3 shr 24) and 0xff], 8) xor KW[r][0]
            r1 = T0[t1 and 0xff] xor shift(T0[(t2 shr 8) and 0xff], 24) xor shift(T0[(r3 shr 16) and 0xff], 16) xor shift(T0[(t0 shr 24) and 0xff], 8) xor KW[r][1]
            r2 = T0[t2 and 0xff] xor shift(T0[(r3 shr 8) and 0xff], 24) xor shift(T0[(t0 shr 16) and 0xff], 16) xor shift(T0[(t1 shr 24) and 0xff], 8) xor KW[r][2]
            r3 = T0[r3 and 0xff] xor shift(T0[(t0 shr 8) and 0xff], 24) xor shift(T0[(t1 shr 16) and 0xff], 16) xor shift(T0[(t2 shr 24) and 0xff], 8) xor KW[r++][3]
            t0 = T0[r0 and 0xff] xor shift(T0[(r1 shr 8) and 0xff], 24) xor shift(T0[(r2 shr 16) and 0xff], 16) xor shift(T0[(r3 shr 24) and 0xff], 8) xor KW[r][0]
            t1 = T0[r1 and 0xff] xor shift(T0[(r2 shr 8) and 0xff], 24) xor shift(T0[(r3 shr 16) and 0xff], 16) xor shift(T0[(r0 shr 24) and 0xff], 8) xor KW[r][1]
            t2 = T0[r2 and 0xff] xor shift(T0[(r3 shr 8) and 0xff], 24) xor shift(T0[(r0 shr 16) and 0xff], 16) xor shift(T0[(r1 shr 24) and 0xff], 8) xor KW[r][2]
            r3 = T0[r3 and 0xff] xor shift(T0[(r0 shr 8) and 0xff], 24) xor shift(T0[(r1 shr 16) and 0xff], 16) xor shift(T0[(r2 shr 24) and 0xff], 8) xor KW[r++][3]
        }

        r0 = T0[t0 and 255] xor shift(T0[(t1 shr 8) and 255], 24) xor shift(T0[(t2 shr 16) and 255], 16) xor shift(T0[(r3 shr 24) and 255], 8) xor KW[r][0]
        r1 = T0[t1 and 255] xor shift(T0[(t2 shr 8) and 255], 24) xor shift(T0[(r3 shr 16) and 255], 16) xor shift(T0[(t0 shr 24) and 255], 8) xor KW[r][1]
        r2 = T0[t2 and 255] xor shift(T0[(r3 shr 8) and 255], 24) xor shift(T0[(t0 shr 16) and 255], 16) xor shift(T0[(t1 shr 24) and 255], 8) xor KW[r][2]
        r3 = T0[r3 and 255] xor shift(T0[(t0 shr 8) and 255], 24) xor shift(T0[(t1 shr 16) and 255], 16) xor shift(T0[(t2 shr 24) and 255], 8) xor KW[r++][3]


        // the final round's table is a simple function of S so we don't use a whole other four tables for it
        C0 = (S[r0 and 255].toInt() and 255) xor ((S[(r1 shr 8) and 255].toInt() and 255) shl 8) xor ((s[(r2 shr 16) and 255].toInt() and 255) shl 16) xor (s[(r3 shr 24) and 255].toInt() shl 24) xor KW[r][0]
        C1 = (s[r1 and 255].toInt() and 255) xor ((S[(r2 shr 8) and 255].toInt() and 255) shl 8) xor ((S[(r3 shr 16) and 255].toInt() and 255) shl 16) xor (s[(r0 shr 24) and 255].toInt() shl 24) xor KW[r][1]
        C2 = (s[r2 and 255].toInt() and 255) xor ((S[(r3 shr 8) and 255].toInt() and 255) shl 8) xor ((S[(r0 shr 16) and 255].toInt() and 255) shl 16) xor (S[(r1 shr 24) and 255].toInt() shl 24) xor KW[r][2]
        C3 = (s[r3 and 255].toInt() and 255) xor ((s[(r0 shr 8) and 255].toInt() and 255) shl 8) xor ((s[(r1 shr 16) and 255].toInt() and 255) shl 16) xor (S[(r2 shr 24) and 255].toInt() shl 24) xor KW[r][3]

        Pack.intToLittleEndian(C0, out, outOff + 0)
        Pack.intToLittleEndian(C1, out, outOff + 4)
        Pack.intToLittleEndian(C2, out, outOff + 8)
        Pack.intToLittleEndian(C3, out, outOff + 12)
    }

    private fun decryptBlock(`in`: ByteArray, inOff: Int, out: ByteArray, outOff: Int, KW: List<IntArray>) {
        var C0 = Pack.littleEndianToInt(`in`, inOff + 0)
        var C1 = Pack.littleEndianToInt(`in`, inOff + 4)
        var C2 = Pack.littleEndianToInt(`in`, inOff + 8)
        var C3 = Pack.littleEndianToInt(`in`, inOff + 12)

        var t0 = C0 xor KW[ROUNDS][0]
        var t1 = C1 xor KW[ROUNDS][1]
        var t2 = C2 xor KW[ROUNDS][2]

        var r = ROUNDS - 1; var r0: Int; var r1: Int; var r2: Int; var r3 = C3 xor KW[ROUNDS][3]
        while (r > 1) {
            r0 = Tinv0[t0 and 255] xor shift( Tinv0[(r3 shr 8) and 255], 24) xor shift( Tinv0[(t2 shr 16) and 255], 16) xor shift( Tinv0[(t1 shr 24) and 255], 8) xor KW[r][0]
            r1 = Tinv0[t1 and 255] xor shift( Tinv0[(t0 shr 8) and 255], 24) xor shift( Tinv0[(r3 shr 16) and 255], 16) xor shift( Tinv0[(t2 shr 24) and 255], 8) xor KW[r][1]
            r2 = Tinv0[t2 and 255] xor shift( Tinv0[(t1 shr 8) and 255], 24) xor shift( Tinv0[(t0 shr 16) and 255], 16) xor shift( Tinv0[(r3 shr 24) and 255], 8) xor KW[r][2]
            r3 = Tinv0[r3 and 255] xor shift( Tinv0[(t2 shr 8) and 255], 24) xor shift( Tinv0[(t1 shr 16) and 255], 16) xor shift( Tinv0[(t0 shr 24) and 255], 8) xor KW[r--][3]
            t0 = Tinv0[r0 and 255] xor shift( Tinv0[(r3 shr 8) and 255], 24) xor shift( Tinv0[(r2 shr 16) and 255], 16) xor shift( Tinv0[(r1 shr 24) and 255], 8) xor KW[r][0]
            t1 = Tinv0[r1 and 255] xor shift( Tinv0[(r0 shr 8) and 255], 24) xor shift( Tinv0[(r3 shr 16) and 255], 16) xor shift( Tinv0[(r2 shr 24) and 255], 8) xor KW[r][1]
            t2 = Tinv0[r2 and 255] xor shift( Tinv0[(r1 shr 8) and 255], 24) xor shift( Tinv0[(r0 shr 16) and 255], 16) xor shift( Tinv0[(r3 shr 24) and 255], 8) xor KW[r][2]
            r3 = Tinv0[r3 and 255] xor shift( Tinv0[(r2 shr 8) and 255], 24) xor shift( Tinv0[(r1 shr 16) and 255], 16) xor shift( Tinv0[(r0 shr 24) and 255], 8) xor KW[r--][3]
        }

        r0 = Tinv0[t0 and 255] xor shift( Tinv0[(r3 shr 8) and 255], 24) xor shift( Tinv0[(t2 shr 16) and 255], 16) xor shift( Tinv0[(t1 shr 24) and 255], 8) xor KW[r][0]
        r1 = Tinv0[t1 and 255] xor shift( Tinv0[(t0 shr 8) and 255], 24) xor shift( Tinv0[(r3 shr 16) and 255], 16) xor shift( Tinv0[(t2 shr 24) and 255], 8) xor KW[r][1]
        r2 = Tinv0[t2 and 255] xor shift( Tinv0[(t1 shr 8) and 255], 24) xor shift( Tinv0[(t0 shr 16) and 255], 16) xor shift( Tinv0[(r3 shr 24) and 255], 8) xor KW[r][2]
        r3 = Tinv0[r3 and 255] xor shift( Tinv0[(t2 shr 8) and 255], 24) xor shift( Tinv0[(t1 shr 16) and 255], 16) xor shift( Tinv0[(t0 shr 24) and 255], 8) xor KW[r][3]


        // the final round's table is a simple function of Si so we don't use a whole other four tables for it
        C0 = (Si[r0 and 255].toInt() and 255) xor ((s[(r3 shr 8) and 255].toInt() and 255) shl 8) xor ((s[(r2 shr 16) and 255].toInt() and 255) shl 16) xor (Si[(r1 shr 24) and 255].toInt() shl 24) xor KW[0][0]
        C1 = (s[r1 and 255].toInt() and 255) xor ((s[(r0 shr 8) and 255].toInt() and 255) shl 8) xor ((Si[(r3 shr 16) and 255].toInt() and 255) shl 16) xor (s[(r2 shr 24) and 255].toInt() shl 24) xor KW[0][1]
        C2 = (s[r2 and 255].toInt() and 255) xor ((Si[(r1 shr 8) and 255].toInt() and 255) shl 8) xor ((Si[(r0 shr 16) and 255].toInt() and 255) shl 16) xor (s[(r3 shr 24) and 255].toInt() shl 24) xor KW[0][2]
        C3 = (Si[r3 and 255].toInt() and 255) xor ((s[(r2 shr 8) and 255].toInt() and 255) shl 8) xor ((s[(r1 shr 16) and 255].toInt() and 255) shl 16) xor (s[(r0 shr 24) and 255].toInt() shl 24) xor KW[0][3]

        Pack.intToLittleEndian(C0, out, outOff + 0)
        Pack.intToLittleEndian(C1, out, outOff + 4)
        Pack.intToLittleEndian(C2, out, outOff + 8)
        Pack.intToLittleEndian(C3, out, outOff + 12)
    }
}































