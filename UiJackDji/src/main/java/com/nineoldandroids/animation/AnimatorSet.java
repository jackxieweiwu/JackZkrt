package com.nineoldandroids.animation;

import android.view.animation.Interpolator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jack_xie on 17-11-2.
 */

public final class AnimatorSet extends Animator {
    private ArrayList<Animator> mPlayingSet = new ArrayList();
    private HashMap<Animator, Node> mNodeMap = new HashMap();
    private ArrayList<AnimatorSet.Node> mNodes = new ArrayList();
    private ArrayList<AnimatorSet.Node> mSortedNodes = new ArrayList();
    private boolean mNeedsSort = true;
    private AnimatorSet.AnimatorSetListener mSetListener = null;
    boolean mTerminated = false;
    private boolean mStarted = false;
    private long mStartDelay = 0L;
    private ValueAnimator mDelayAnim = null;
    private long mDuration = -1L;

    public AnimatorSet() {
    }

    public void playTogether(Animator... var1) {
        if(var1 != null) {
            this.mNeedsSort = true;
            AnimatorSet.Builder var2 = this.play(var1[0]);

            for(int var3 = 1; var3 < var1.length; ++var3) {
                var2.with(var1[var3]);
            }
        }

    }

    public void playTogether(Collection<Animator> var1) {
        if(var1 != null && var1.size() > 0) {
            this.mNeedsSort = true;
            AnimatorSet.Builder var2 = null;
            Iterator var3 = var1.iterator();

            while(var3.hasNext()) {
                Animator var4 = (Animator)var3.next();
                if(var2 == null) {
                    var2 = this.play(var4);
                } else {
                    var2.with(var4);
                }
            }
        }

    }

    public void playSequentially(Animator... var1) {
        if(var1 != null) {
            this.mNeedsSort = true;
            if(var1.length == 1) {
                this.play(var1[0]);
            } else {
                for(int var2 = 0; var2 < var1.length - 1; ++var2) {
                    this.play(var1[var2]).before(var1[var2 + 1]);
                }
            }
        }

    }

    public void playSequentially(List<Animator> var1) {
        if(var1 != null && var1.size() > 0) {
            this.mNeedsSort = true;
            if(var1.size() == 1) {
                this.play((Animator)var1.get(0));
            } else {
                for(int var2 = 0; var2 < var1.size() - 1; ++var2) {
                    this.play((Animator)var1.get(var2)).before((Animator)var1.get(var2 + 1));
                }
            }
        }

    }

    public ArrayList<Animator> getChildAnimations() {
        ArrayList var1 = new ArrayList();
        Iterator var2 = this.mNodes.iterator();

        while(var2.hasNext()) {
            AnimatorSet.Node var3 = (AnimatorSet.Node)var2.next();
            var1.add(var3.animation);
        }

        return var1;
    }

    public void setTarget(Object var1) {
        Iterator var2 = this.mNodes.iterator();

        while(var2.hasNext()) {
            AnimatorSet.Node var3 = (AnimatorSet.Node)var2.next();
            Animator var4 = var3.animation;
            if(var4 instanceof AnimatorSet) {
                ((AnimatorSet)var4).setTarget(var1);
            } else if(var4 instanceof ObjectAnimator) {
                ((ObjectAnimator)var4).setTarget(var1);
            }
        }

    }

    public void setInterpolator(Interpolator var1) {
        Iterator var2 = this.mNodes.iterator();

        while(var2.hasNext()) {
            AnimatorSet.Node var3 = (AnimatorSet.Node)var2.next();
            var3.animation.setInterpolator(var1);
        }

    }

    public AnimatorSet.Builder play(Animator var1) {
        if(var1 != null) {
            this.mNeedsSort = true;
            return new AnimatorSet.Builder(var1);
        } else {
            return null;
        }
    }

    public void cancel() {
        this.mTerminated = true;
        if(this.isStarted()) {
            ArrayList var1 = null;
            Iterator var2;
            AnimatorListener var3;
            if(this.mListeners != null) {
                var1 = (ArrayList)this.mListeners.clone();
                var2 = var1.iterator();

                while(var2.hasNext()) {
                    var3 = (AnimatorListener)var2.next();
                    var3.onAnimationCancel(this);
                }
            }

            if(this.mDelayAnim != null && this.mDelayAnim.isRunning()) {
                this.mDelayAnim.cancel();
            } else if(this.mSortedNodes.size() > 0) {
                var2 = this.mSortedNodes.iterator();

                while(var2.hasNext()) {
                    AnimatorSet.Node var4 = (AnimatorSet.Node)var2.next();
                    var4.animation.cancel();
                }
            }

            if(var1 != null) {
                var2 = var1.iterator();

                while(var2.hasNext()) {
                    var3 = (AnimatorListener)var2.next();
                    var3.onAnimationEnd(this);
                }
            }

            this.mStarted = false;
        }

    }

    public void end() {
        this.mTerminated = true;
        if(this.isStarted()) {
            Iterator var1;
            AnimatorSet.Node var2;
            if(this.mSortedNodes.size() != this.mNodes.size()) {
                this.sortNodes();

                for(var1 = this.mSortedNodes.iterator(); var1.hasNext(); var2.animation.addListener(this.mSetListener)) {
                    var2 = (AnimatorSet.Node)var1.next();
                    if(this.mSetListener == null) {
                        this.mSetListener = new AnimatorSet.AnimatorSetListener(this);
                    }
                }
            }

            if(this.mDelayAnim != null) {
                this.mDelayAnim.cancel();
            }

            if(this.mSortedNodes.size() > 0) {
                var1 = this.mSortedNodes.iterator();

                while(var1.hasNext()) {
                    var2 = (AnimatorSet.Node)var1.next();
                    var2.animation.end();
                }
            }

            if(this.mListeners != null) {
                ArrayList var4 = (ArrayList)this.mListeners.clone();
                Iterator var5 = var4.iterator();

                while(var5.hasNext()) {
                    AnimatorListener var3 = (AnimatorListener)var5.next();
                    var3.onAnimationEnd(this);
                }
            }

            this.mStarted = false;
        }

    }

    public boolean isRunning() {
        Iterator var1 = this.mNodes.iterator();

        AnimatorSet.Node var2;
        do {
            if(!var1.hasNext()) {
                return false;
            }

            var2 = (AnimatorSet.Node)var1.next();
        } while(!var2.animation.isRunning());

        return true;
    }

    public boolean isStarted() {
        return this.mStarted;
    }

    public long getStartDelay() {
        return this.mStartDelay;
    }

    public void setStartDelay(long var1) {
        this.mStartDelay = var1;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public AnimatorSet setDuration(long var1) {
        if(var1 < 0L) {
            throw new IllegalArgumentException("duration must be a value of zero or greater");
        } else {
            Iterator var3 = this.mNodes.iterator();

            while(var3.hasNext()) {
                AnimatorSet.Node var4 = (AnimatorSet.Node)var3.next();
                var4.animation.setDuration(var1);
            }

            this.mDuration = var1;
            return this;
        }
    }

    public void setupStartValues() {
        Iterator var1 = this.mNodes.iterator();

        while(var1.hasNext()) {
            AnimatorSet.Node var2 = (AnimatorSet.Node)var1.next();
            var2.animation.setupStartValues();
        }

    }

    public void setupEndValues() {
        Iterator var1 = this.mNodes.iterator();

        while(var1.hasNext()) {
            AnimatorSet.Node var2 = (AnimatorSet.Node)var1.next();
            var2.animation.setupEndValues();
        }

    }

    public void start() {
        this.mTerminated = false;
        this.mStarted = true;
        this.sortNodes();
        int var1 = this.mSortedNodes.size();

        label104:
        for(int var2 = 0; var2 < var1; ++var2) {
            AnimatorSet.Node var3 = (AnimatorSet.Node)this.mSortedNodes.get(var2);
            ArrayList var4 = var3.animation.getListeners();
            if(var4 != null && var4.size() > 0) {
                ArrayList var5 = new ArrayList(var4);
                Iterator var6 = var5.iterator();

                while(true) {
                    AnimatorListener var7;
                    do {
                        if(!var6.hasNext()) {
                            continue label104;
                        }

                        var7 = (AnimatorListener)var6.next();
                    } while(!(var7 instanceof AnimatorSet.DependencyListener) && !(var7 instanceof AnimatorSet.AnimatorSetListener));

                    var3.animation.removeListener(var7);
                }
            }
        }

        final ArrayList var8 = new ArrayList();

        AnimatorSet.Node var11;
        int var14;
        for(int var9 = 0; var9 < var1; ++var9) {
            var11 = (AnimatorSet.Node)this.mSortedNodes.get(var9);
            if(this.mSetListener == null) {
                this.mSetListener = new AnimatorSet.AnimatorSetListener(this);
            }

            if(var11.dependencies != null && var11.dependencies.size() != 0) {
                var14 = var11.dependencies.size();

                for(int var15 = 0; var15 < var14; ++var15) {
                    AnimatorSet.Dependency var16 = (AnimatorSet.Dependency)var11.dependencies.get(var15);
                    var16.node.animation.addListener(new AnimatorSet.DependencyListener(this, var11, var16.rule));
                }

                var11.tmpDependencies = (ArrayList)var11.dependencies.clone();
            } else {
                var8.add(var11);
            }

            var11.animation.addListener(this.mSetListener);
        }

        if(this.mStartDelay <= 0L) {
            Iterator var10 = var8.iterator();

            while(var10.hasNext()) {
                var11 = (AnimatorSet.Node)var10.next();
                var11.animation.start();
                this.mPlayingSet.add(var11.animation);
            }
        } else {
            this.mDelayAnim = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
            this.mDelayAnim.setDuration(this.mStartDelay);
            this.mDelayAnim.addListener(new AnimatorListenerAdapter() {
                boolean canceled = false;

                public void onAnimationCancel(Animator var1) {
                    this.canceled = true;
                }

                public void onAnimationEnd(Animator var1) {
                    if(!this.canceled) {
                        int var2 = var8.size();

                        for(int var3 = 0; var3 < var2; ++var3) {
                            AnimatorSet.Node var4 = (AnimatorSet.Node)var8.get(var3);
                            var4.animation.start();
                            AnimatorSet.this.mPlayingSet.add(var4.animation);
                        }
                    }

                }
            });
            this.mDelayAnim.start();
        }

        ArrayList var12;
        int var13;
        if(this.mListeners != null) {
            var12 = (ArrayList)this.mListeners.clone();
            var13 = var12.size();

            for(var14 = 0; var14 < var13; ++var14) {
                ((AnimatorListener)var12.get(var14)).onAnimationStart(this);
            }
        }

        if(this.mNodes.size() == 0 && this.mStartDelay == 0L) {
            this.mStarted = false;
            if(this.mListeners != null) {
                var12 = (ArrayList)this.mListeners.clone();
                var13 = var12.size();

                for(var14 = 0; var14 < var13; ++var14) {
                    ((AnimatorListener)var12.get(var14)).onAnimationEnd(this);
                }
            }
        }

    }

    public AnimatorSet clone() {
        AnimatorSet var1 = (AnimatorSet)super.clone();
        var1.mNeedsSort = true;
        var1.mTerminated = false;
        var1.mStarted = false;
        var1.mPlayingSet = new ArrayList();
        var1.mNodeMap = new HashMap();
        var1.mNodes = new ArrayList();
        var1.mSortedNodes = new ArrayList();
        HashMap var2 = new HashMap();
        Iterator var3 = this.mNodes.iterator();

        while(true) {
            ArrayList var6;
            ArrayList var7;
            Iterator var8;
            AnimatorListener var9;
            do {
                do {
                    AnimatorSet.Node var4;
                    AnimatorSet.Node var5;
                    if(!var3.hasNext()) {
                        var3 = this.mNodes.iterator();

                        while(true) {
                            do {
                                if(!var3.hasNext()) {
                                    return var1;
                                }

                                var4 = (AnimatorSet.Node)var3.next();
                                var5 = (AnimatorSet.Node)var2.get(var4);
                            } while(var4.dependencies == null);

                            Iterator var10 = var4.dependencies.iterator();

                            while(var10.hasNext()) {
                                AnimatorSet.Dependency var11 = (AnimatorSet.Dependency)var10.next();
                                AnimatorSet.Node var12 = (AnimatorSet.Node)var2.get(var11.node);
                                AnimatorSet.Dependency var13 = new AnimatorSet.Dependency(var12, var11.rule);
                                var5.addDependency(var13);
                            }
                        }
                    }

                    var4 = (AnimatorSet.Node)var3.next();
                    var5 = var4.clone();
                    var2.put(var4, var5);
                    var1.mNodes.add(var5);
                    var1.mNodeMap.put(var5.animation, var5);
                    var5.dependencies = null;
                    var5.tmpDependencies = null;
                    var5.nodeDependents = null;
                    var5.nodeDependencies = null;
                    var6 = var5.animation.getListeners();
                } while(var6 == null);

                var7 = null;
                var8 = var6.iterator();

                while(var8.hasNext()) {
                    var9 = (AnimatorListener)var8.next();
                    if(var9 instanceof AnimatorSet.AnimatorSetListener) {
                        if(var7 == null) {
                            var7 = new ArrayList();
                        }

                        var7.add(var9);
                    }
                }
            } while(var7 == null);

            var8 = var7.iterator();

            while(var8.hasNext()) {
                var9 = (AnimatorListener)var8.next();
                var6.remove(var9);
            }
        }
    }

    private void sortNodes() {
        int var2;
        int var5;
        int var13;
        if(this.mNeedsSort) {
            this.mSortedNodes.clear();
            ArrayList var1 = new ArrayList();
            var2 = this.mNodes.size();

            for(int var3 = 0; var3 < var2; ++var3) {
                AnimatorSet.Node var4 = (AnimatorSet.Node)this.mNodes.get(var3);
                if(var4.dependencies == null || var4.dependencies.size() == 0) {
                    var1.add(var4);
                }
            }

            ArrayList var11 = new ArrayList();

            while(var1.size() > 0) {
                var13 = var1.size();

                for(var5 = 0; var5 < var13; ++var5) {
                    AnimatorSet.Node var6 = (AnimatorSet.Node)var1.get(var5);
                    this.mSortedNodes.add(var6);
                    if(var6.nodeDependents != null) {
                        int var7 = var6.nodeDependents.size();

                        for(int var8 = 0; var8 < var7; ++var8) {
                            AnimatorSet.Node var9 = (AnimatorSet.Node)var6.nodeDependents.get(var8);
                            var9.nodeDependencies.remove(var6);
                            if(var9.nodeDependencies.size() == 0) {
                                var11.add(var9);
                            }
                        }
                    }
                }

                var1.clear();
                var1.addAll(var11);
                var11.clear();
            }

            this.mNeedsSort = false;
            if(this.mSortedNodes.size() != this.mNodes.size()) {
                throw new IllegalStateException("Circular dependencies cannot exist in AnimatorSet");
            }
        } else {
            int var10 = this.mNodes.size();

            for(var2 = 0; var2 < var10; ++var2) {
                AnimatorSet.Node var12 = (AnimatorSet.Node)this.mNodes.get(var2);
                if(var12.dependencies != null && var12.dependencies.size() > 0) {
                    var13 = var12.dependencies.size();

                    for(var5 = 0; var5 < var13; ++var5) {
                        AnimatorSet.Dependency var14 = (AnimatorSet.Dependency)var12.dependencies.get(var5);
                        if(var12.nodeDependencies == null) {
                            var12.nodeDependencies = new ArrayList();
                        }

                        if(!var12.nodeDependencies.contains(var14.node)) {
                            var12.nodeDependencies.add(var14.node);
                        }
                    }
                }

                var12.done = false;
            }
        }

    }

    public class Builder {
        private AnimatorSet.Node mCurrentNode;

        Builder(Animator var2) {
            this.mCurrentNode = (AnimatorSet.Node)AnimatorSet.this.mNodeMap.get(var2);
            if(this.mCurrentNode == null) {
                this.mCurrentNode = new AnimatorSet.Node(var2);
                AnimatorSet.this.mNodeMap.put(var2, this.mCurrentNode);
                AnimatorSet.this.mNodes.add(this.mCurrentNode);
            }

        }

        public AnimatorSet.Builder with(Animator var1) {
            AnimatorSet.Node var2 = (AnimatorSet.Node)AnimatorSet.this.mNodeMap.get(var1);
            if(var2 == null) {
                var2 = new AnimatorSet.Node(var1);
                AnimatorSet.this.mNodeMap.put(var1, var2);
                AnimatorSet.this.mNodes.add(var2);
            }

            AnimatorSet.Dependency var3 = new AnimatorSet.Dependency(this.mCurrentNode, 0);
            var2.addDependency(var3);
            return this;
        }

        public AnimatorSet.Builder before(Animator var1) {
            AnimatorSet.Node var2 = (AnimatorSet.Node)AnimatorSet.this.mNodeMap.get(var1);
            if(var2 == null) {
                var2 = new AnimatorSet.Node(var1);
                AnimatorSet.this.mNodeMap.put(var1, var2);
                AnimatorSet.this.mNodes.add(var2);
            }

            AnimatorSet.Dependency var3 = new AnimatorSet.Dependency(this.mCurrentNode, 1);
            var2.addDependency(var3);
            return this;
        }

        public AnimatorSet.Builder after(Animator var1) {
            AnimatorSet.Node var2 = (AnimatorSet.Node)AnimatorSet.this.mNodeMap.get(var1);
            if(var2 == null) {
                var2 = new AnimatorSet.Node(var1);
                AnimatorSet.this.mNodeMap.put(var1, var2);
                AnimatorSet.this.mNodes.add(var2);
            }

            AnimatorSet.Dependency var3 = new AnimatorSet.Dependency(var2, 1);
            this.mCurrentNode.addDependency(var3);
            return this;
        }

        public AnimatorSet.Builder after(long var1) {
            ValueAnimator var3 = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
            var3.setDuration(var1);
            this.after(var3);
            return this;
        }
    }

    private static class Node implements Cloneable {
        public Animator animation;
        public ArrayList<AnimatorSet.Dependency> dependencies = null;
        public ArrayList<AnimatorSet.Dependency> tmpDependencies = null;
        public ArrayList<AnimatorSet.Node> nodeDependencies = null;
        public ArrayList<AnimatorSet.Node> nodeDependents = null;
        public boolean done = false;

        public Node(Animator var1) {
            this.animation = var1;
        }

        public void addDependency(AnimatorSet.Dependency var1) {
            if(this.dependencies == null) {
                this.dependencies = new ArrayList();
                this.nodeDependencies = new ArrayList();
            }

            this.dependencies.add(var1);
            if(!this.nodeDependencies.contains(var1.node)) {
                this.nodeDependencies.add(var1.node);
            }

            AnimatorSet.Node var2 = var1.node;
            if(var2.nodeDependents == null) {
                var2.nodeDependents = new ArrayList();
            }

            var2.nodeDependents.add(this);
        }

        public AnimatorSet.Node clone() {
            try {
                AnimatorSet.Node var1 = (AnimatorSet.Node)super.clone();
                var1.animation = this.animation.clone();
                return var1;
            } catch (CloneNotSupportedException var2) {
                throw new AssertionError();
            }
        }
    }

    private static class Dependency {
        static final int WITH = 0;
        static final int AFTER = 1;
        public AnimatorSet.Node node;
        public int rule;

        public Dependency(AnimatorSet.Node var1, int var2) {
            this.node = var1;
            this.rule = var2;
        }
    }

    private class AnimatorSetListener implements AnimatorListener {
        private AnimatorSet mAnimatorSet;

        AnimatorSetListener(AnimatorSet var2) {
            this.mAnimatorSet = var2;
        }

        public void onAnimationCancel(Animator var1) {
            if(!AnimatorSet.this.mTerminated && AnimatorSet.this.mPlayingSet.size() == 0 && AnimatorSet.this.mListeners != null) {
                int var2 = AnimatorSet.this.mListeners.size();

                for(int var3 = 0; var3 < var2; ++var3) {
                    ((AnimatorListener)AnimatorSet.this.mListeners.get(var3)).onAnimationCancel(this.mAnimatorSet);
                }
            }

        }

        public void onAnimationEnd(Animator var1) {
            var1.removeListener(this);
            AnimatorSet.this.mPlayingSet.remove(var1);
            AnimatorSet.Node var2 = (AnimatorSet.Node)this.mAnimatorSet.mNodeMap.get(var1);
            var2.done = true;
            if(!AnimatorSet.this.mTerminated) {
                ArrayList var3 = this.mAnimatorSet.mSortedNodes;
                boolean var4 = true;
                int var5 = var3.size();

                for(int var6 = 0; var6 < var5; ++var6) {
                    if(!((AnimatorSet.Node)var3.get(var6)).done) {
                        var4 = false;
                        break;
                    }
                }

                if(var4) {
                    if(AnimatorSet.this.mListeners != null) {
                        ArrayList var9 = (ArrayList)AnimatorSet.this.mListeners.clone();
                        int var7 = var9.size();

                        for(int var8 = 0; var8 < var7; ++var8) {
                            ((AnimatorListener)var9.get(var8)).onAnimationEnd(this.mAnimatorSet);
                        }
                    }

                    this.mAnimatorSet.mStarted = false;
                }
            }

        }

        public void onAnimationRepeat(Animator var1) {
        }

        public void onAnimationStart(Animator var1) {
        }
    }

    private static class DependencyListener implements AnimatorListener {
        private AnimatorSet mAnimatorSet;
        private AnimatorSet.Node mNode;
        private int mRule;

        public DependencyListener(AnimatorSet var1, AnimatorSet.Node var2, int var3) {
            this.mAnimatorSet = var1;
            this.mNode = var2;
            this.mRule = var3;
        }

        public void onAnimationCancel(Animator var1) {
        }

        public void onAnimationEnd(Animator var1) {
            if(this.mRule == 1) {
                this.startIfReady(var1);
            }

        }

        public void onAnimationRepeat(Animator var1) {
        }

        public void onAnimationStart(Animator var1) {
            if(this.mRule == 0) {
                this.startIfReady(var1);
            }

        }

        private void startIfReady(Animator var1) {
            if(!this.mAnimatorSet.mTerminated) {
                AnimatorSet.Dependency var2 = null;
                int var3 = this.mNode.tmpDependencies.size();

                for(int var4 = 0; var4 < var3; ++var4) {
                    AnimatorSet.Dependency var5 = (AnimatorSet.Dependency)this.mNode.tmpDependencies.get(var4);
                    if(var5.rule == this.mRule && var5.node.animation == var1) {
                        var2 = var5;
                        var1.removeListener(this);
                        break;
                    }
                }

                this.mNode.tmpDependencies.remove(var2);
                if(this.mNode.tmpDependencies.size() == 0) {
                    this.mNode.animation.start();
                    this.mAnimatorSet.mPlayingSet.add(this.mNode.animation);
                }

            }
        }
    }
}
