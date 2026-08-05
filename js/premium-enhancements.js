/**
 * PLUS33 Coffee Franchise - Safe Premium Dynamic Enhancements
 * Non-intrusive enhancement layer preserving all existing code, layouts & interactions.
 */

(function () {
    "use strict";

    // Wait for DOM and libraries to be ready
    document.addEventListener("DOMContentLoaded", function () {
        if (typeof gsap === "undefined" || typeof ScrollTrigger === "undefined") {
            return;
        }

        gsap.registerPlugin(ScrollTrigger);

        // Respect prefers-reduced-motion
        if (window.matchMedia("(prefers-reduced-motion: reduce)").matches) {
            return;
        }

        const mm = gsap.matchMedia();

        // 1. Scroll Progress Bar (Minimal Champagne Gold Line at top)
        initScrollProgress();

        // 2. Micro-Interactions for Buttons
        initButtonInteractions();

        // 3. Decorative Parallax Layers
        initParallaxLayers(mm);

        // 4. Safe Section Reveals (Eyebrows, Headings, Descriptions)
        initSectionReveals(mm);
    });

    function initScrollProgress() {
        if (document.querySelector(".plus33-progress-bar")) return;

        const progressBar = document.createElement("div");
        progressBar.className = "plus33-progress-bar";
        progressBar.style.cssText = `
            position: fixed;
            top: 0;
            left: 0;
            height: 2px;
            width: 0%;
            background: linear-gradient(90deg, #D4AF37, #F97316);
            z-index: 9999;
            pointer-events: none;
            transition: width 0.1s linear;
        `;
        document.body.appendChild(progressBar);

        window.addEventListener("scroll", function () {
            const winScroll = document.documentElement.scrollTop || document.body.scrollTop;
            const height = document.documentElement.scrollHeight - document.documentElement.clientHeight;
            if (height > 0) {
                const scrolled = (winScroll / height) * 100;
                progressBar.style.width = scrolled + "%";
            }
        }, { passive: true });
    }

    function initButtonInteractions() {
        const buttons = document.querySelectorAll(
            "button:not(.swiper-button-next):not(.swiper-button-prev):not(.partner-deck-btn), .btn:not(.no-anim), .cta-btn"
        );

        buttons.forEach((btn) => {
            btn.addEventListener("mouseenter", () => {
                if (window.innerWidth > 768) {
                    gsap.to(btn, {
                        y: -2,
                        scale: 1.015,
                        duration: 0.25,
                        ease: "power2.out",
                        overwrite: "auto"
                    });
                }
            });

            btn.addEventListener("mouseleave", () => {
                if (window.innerWidth > 768) {
                    gsap.to(btn, {
                        y: 0,
                        scale: 1,
                        duration: 0.3,
                        ease: "power2.out",
                        overwrite: "auto"
                    });
                }
            });

            btn.addEventListener("touchstart", () => {
                gsap.to(btn, {
                    scale: 0.97,
                    duration: 0.14,
                    ease: "power2.out",
                    overwrite: "auto"
                });
            }, { passive: true });

            btn.addEventListener("touchend", () => {
                gsap.to(btn, {
                    scale: 1,
                    duration: 0.2,
                    ease: "power2.out",
                    overwrite: "auto"
                });
            }, { passive: true });
        });
    }

    function initParallaxLayers(mm) {
        mm.add("(min-width: 769px)", () => {
            const parallaxTargets = document.querySelectorAll(
                ".s5-particles, .s6-particles, .s6-light-rays, .ambient-glow-layer, .decorative-bg-particle"
            );

            parallaxTargets.forEach((target) => {
                gsap.to(target, {
                    yPercent: -6,
                    ease: "none",
                    scrollTrigger: {
                        trigger: target.parentElement || target,
                        start: "top bottom",
                        end: "bottom top",
                        scrub: 1
                    }
                });
            });
        });
    }

    function initSectionReveals(mm) {
        // Desktop Motion Settings
        mm.add("(min-width: 769px)", () => {
            const sections = document.querySelectorAll("section");

            sections.forEach((sec) => {
                // Protect hero, revenue, and sections with custom headings from initial autoAlpha hiding
                if (sec.id === "hero" || sec.classList.contains("hero-section") || sec.id === "revenue") return;

                const eyebrow = sec.querySelector(".section-eyebrow, .st-eyebrow, .s6-eyebrow, .ben-eyebrow, .req-eyebrow");
                const desc = sec.querySelector(".section-desc, .st-desc, .s6-desc, .ben-desc, .req-desc");

                const elementsToAnimate = [eyebrow, desc].filter(Boolean);

                if (elementsToAnimate.length > 0) {
                    gsap.from(elementsToAnimate, {
                        autoAlpha: 0,
                        y: 20,
                        duration: 0.75,
                        stagger: 0.08,
                        ease: "power3.out",
                        scrollTrigger: {
                            trigger: sec,
                            start: "top 85%",
                            once: true
                        }
                    });
                }
            });
        });

        // Mobile Motion Settings (Lighter & subtle)
        mm.add("(max-width: 768px)", () => {
            const sections = document.querySelectorAll("section");

            sections.forEach((sec) => {
                if (sec.id === "hero" || sec.classList.contains("hero-section") || sec.id === "revenue") return;

                const eyebrow = sec.querySelector(".section-eyebrow, .st-eyebrow, .s6-eyebrow, .ben-eyebrow, .req-eyebrow");
                const desc = sec.querySelector(".section-desc, .st-desc, .s6-desc, .ben-desc, .req-desc");

                const elementsToAnimate = [eyebrow, desc].filter(Boolean);

                if (elementsToAnimate.length > 0) {
                    gsap.from(elementsToAnimate, {
                        autoAlpha: 0,
                        y: 14,
                        duration: 0.6,
                        stagger: 0.06,
                        ease: "power3.out",
                        scrollTrigger: {
                            trigger: sec,
                            start: "top 90%",
                            once: true
                        }
                    });
                }
            });
        });
    }
})();
