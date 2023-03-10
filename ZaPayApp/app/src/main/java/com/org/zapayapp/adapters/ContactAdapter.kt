package com.org.zapayapp.adapters

import com.org.zapayapp.listener.ContactListener
import com.org.zapayapp.model.ContactModel
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.widget.LinearLayout
import com.org.zapayapp.R
import android.view.ViewGroup
import android.view.LayoutInflater
import io.branch.indexing.BranchUniversalObject
import com.org.zapayapp.utils.Const.Var
import io.branch.referral.util.ShareSheetStyle
import android.app.Activity
import android.content.Context
import android.os.DeadObjectException
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.org.zapayapp.utils.CommonMethods
import com.org.zapayapp.utils.Const
import io.branch.referral.Branch.BranchLinkShareListener
import io.branch.referral.BranchError
import io.branch.referral.util.LinkProperties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactAdapter(
    private val context: Context,
    private val contactListener: ContactListener,
    private val contactList: MutableList<out ContactModel>,
    private val onClickListener: View.OnClickListener
) : RecyclerView.Adapter<ContactAdapter.MyHolder>() {
    var selectedPos = -1
        private set
    private var forWhat = ""
    private val resourceCheck: Int
    private val resourceUnCheck: Int

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contactTxtName: TextView
        internal val btnInvite: TextView
        internal val contactImgSelect: ImageView
        internal val llView: LinearLayout

        init {
            llView = itemView.findViewById(R.id.llView)
            contactTxtName = itemView.findViewById(R.id.contactTxtName)
            contactImgSelect = itemView.findViewById(R.id.contactImgSelect)
            btnInvite = itemView.findViewById(R.id.btnInvite)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_contact_list, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val model = contactList[position]
        val isInvite = model.isInvite
        if (selectedPos == position) {
            holder.contactImgSelect.setImageResource(resourceCheck)
            holder.contactTxtName.isSelected = true
        } else {
            holder.contactImgSelect.setImageResource(resourceUnCheck)
            holder.contactTxtName.isSelected = false
        }
        if (forWhat.equals(context.getString(R.string.negotiation), ignoreCase = true)) {
            holder.llView.isClickable = false
        } else {
            if (isInvite == 0) {
                holder.btnInvite.visibility = View.GONE
                holder.contactImgSelect.visibility = View.VISIBLE
                holder.llView.isClickable = true
                holder.llView.setTag(position)
                holder.llView.setOnClickListener(onClickListener)

            } else {
                holder.btnInvite.visibility = View.VISIBLE
                holder.contactImgSelect.visibility = View.GONE
                holder.btnInvite.setOnClickListener {
                    Log.e("DEFAULT_SHARE_MSG", "Var.DEFAULT_SHARE_MSG = " + Var.DEFAULT_SHARE_MSG);

                    try {
                        val `object` = BranchUniversalObject()
                        `object`.title = "ZaPay"
                        `object`.setContentDescription(Var.DEFAULT_SHARE_MSG)
                        val linkProperties = LinkProperties()
                        val shareSheetStyle = ShareSheetStyle(context, "ZaPay", Var.DEFAULT_SHARE_MSG)
                            .setAsFullWidthStyle(true)
                            .setStyleResourceID(R.style.bottomSheetStyleWrapper)
                            .setSharingTitle("Invite Friends")
                        `object`.showShareSheet(
                            (context as Activity),
                            linkProperties,
                            shareSheetStyle,
                            object : BranchLinkShareListener {
                                override fun onShareLinkDialogLaunched() {}
                                override fun onShareLinkDialogDismissed() {}
                                override fun onLinkShareResponse(
                                    sharedLink: String?,
                                    sharedChannel: String?,
                                    error: BranchError?
                                ) {
                                }

                                override fun onChannelSelected(channelName: String) {}
                            })

                    } catch (e: java.lang.Exception) {
                        Const.logMsg("eeeeee Share Link = " + e.message)
                    }
                }
            }
        }

        try {
            if (model.mobileContactName != null && model.mobileContactName.isNotEmpty()) {
                holder.contactTxtName.text = model.mobileContactName
            }
        } catch (e: java.lang.Exception) {
            Const.logMsg("eeeeee = " + e.message)
        }

    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    val selected: String?
        get() = if (selectedPos != -1) {
            contactList[selectedPos].mobileContactName
        } else null

    fun setSelected(position: Int, forWhat: String) {
        this.forWhat = forWhat
        selectedPos = position
        notifyItemChanged(position)
    }

    fun setSelected(position: Int) {
        selectedPos = position
    }

    init {
        resourceCheck = R.mipmap.ic_check_select
        resourceUnCheck = R.drawable.checkbox_unselect
    }
}